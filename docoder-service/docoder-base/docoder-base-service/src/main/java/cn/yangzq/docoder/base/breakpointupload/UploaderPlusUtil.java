package cn.yangzq.docoder.base.breakpointupload;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.yangzq.docoder.base.config.DocoderConfig;
import cn.yangzq.docoder.common.core.exception.BusinessException;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Data
@Component
public class UploaderPlusUtil {

    private DocoderConfig config;

    @Autowired
    public void setConfig(DocoderConfig config) {
        this.config = config;
        UPLOAD_TEMP_DIR = config.getUploadTempDir();
    }

    //临时文件目录
    private static String UPLOAD_TEMP_DIR = "";
    //当前分片信息
    //private AtomicReference<FileChunk> chunk = new AtomicReference<>();

    //第几分片
    //private AtomicInteger partNum = new AtomicInteger();

    //文件唯一标识
    private String identifier = null;

    //是否完成
    private AtomicBoolean mergeSuccess = new AtomicBoolean(false);

    //任务列表
    private static final ConcurrentHashMap<String, UploaderPlusUtil> TASK_LIST = new ConcurrentHashMap<>();

    //上传的片数index
    private ConcurrentHashSet<Integer> partIndex = new ConcurrentHashSet<>();

    //merge成功后的文件
    private File mergeFile;

    //任务最后请求时间
    private Date lastAccessTime = new Date();

    //清理任务的id
    private static String clearJobId = null;

    //每个任务12小时后没请求操作自动清理
    private static final int HOUR_AFTER = 12;

    //任务是否完成
    private AtomicBoolean taskCompletion = new AtomicBoolean(false);

    //该任务所对应的数据
    private AtomicReference<Object> taskData = new AtomicReference<>();

    static {
        executeClearTask();
    }

    //定时任务清理任务
    public static void executeClearTask(){//*/5 * * * * ?
        clearJobId = CronUtil.schedule("0 0 0/12 * * ?", (Task) () -> {
            if(StrUtil.isBlank(UPLOAD_TEMP_DIR)){
                return;
            }
            long now = new Date().getTime();
            //获取任务列表和对应的文件
            if (CollectionUtil.isNotEmpty(TASK_LIST)) {
                //清理任务和对应临时文件
                TASK_LIST.forEach((taskKey,task)->{
                    Date lastAccessTime = task.getLastAccessTime();
                    DateTime hourAfterTime = DateUtil.offsetHour(lastAccessTime, HOUR_AFTER);
                    long afterMs = now - hourAfterTime.getTime();
                    if(afterMs>=0){
                        TASK_LIST.remove(taskKey);
                        if (CollectionUtil.isEmpty(TASK_LIST)) {
                            task.deleteDir(UPLOAD_TEMP_DIR+File.separator+task.identifier);
                            //CronUtil.remove(clearJobId);
                            task = null;
                        }
                    }
                });
            }
            //临时文件存放目录的过期未访问文件
            File tempDir = new File(UPLOAD_TEMP_DIR);
            File[] tempFiles = tempDir.listFiles();
            tempFiles = tempFiles==null?new File[0]:tempFiles;
            if(tempFiles.length>0){
                for(File tempFile:tempFiles){
                    long lastModified = tempFile.lastModified();
                    long hourAfterTime = DateUtil.offsetHour(new Date(lastModified), HOUR_AFTER).getTime();
                    if(now-hourAfterTime>=0){
                        deleteDir(UPLOAD_TEMP_DIR+File.separator+tempFile.getName());
                    }
                }
            }
        });
    }

    /**
     * 初始化一个任务
     *
     * @param chunk
     * @return
     */
    public UploaderPlusUtil initTask(FileChunk chunk) {
        String md5Str = chunk.getMd5Str();
        UploaderPlusUtil uploaderPlusUtil = TASK_LIST.get(md5Str);
        if (uploaderPlusUtil == null) {
            synchronized (UploaderPlusUtil.class) {
                if (uploaderPlusUtil == null) {
                    uploaderPlusUtil = new UploaderPlusUtil();
                    uploaderPlusUtil.setIdentifier(chunk.getIdentifier());
                    //uploaderUtil.getChunk().set(chunk);
                    TASK_LIST.put(md5Str, uploaderPlusUtil);
                }
            }
        }
        return uploaderPlusUtil;
    }

    /**
     * 获取上传状态
     *
     * @param chunk
     * @return
     */
    public UploaderPlusUtil getUploadStatus(FileChunk chunk) {
        //任务存放目录
        String saveDir = UPLOAD_TEMP_DIR + (UPLOAD_TEMP_DIR.endsWith(File.separator) ? "" : File.separator) + chunk.getIdentifier();
        File saveDirFile = new File(saveDir);
        //part存放目录
        String savePartDir = saveDir + File.separator + "parts";
        File savePartDirFile = new File(savePartDir);
        //任务已存储的文件
        File[] dirFiles = saveDirFile.listFiles();
        dirFiles  = dirFiles==null?new File[0]:dirFiles;
        int dirSize = dirFiles.length;
        //part已存储的文件
        File[] partFiles = savePartDirFile.listFiles();
        partFiles  = partFiles==null?new File[0]:partFiles;
        int partSize = partFiles.length;
        //是否有过上传
        boolean isStore = dirSize==2 ||(dirSize==1&& (dirFiles[0].isDirectory()&&partSize>0 ||(dirFiles[0].isFile())))|| partSize>0;
        //已存储的分片数
        int partNum = this.partIndex.size();
        //新的上传任务 直接返回
        if(partNum==0 && !isStore){
            return this;
        }
        //在任务列表中的任务 直接返回
        if(partNum>0){
            return this;
        }
        //dirSize==2 merge被中断 重新merge
        if(dirSize==2){
            File dir = dirFiles[0].isDirectory()?dirFiles[0]:dirFiles[1];
            File mergeFile = dirFiles[0].isFile()?dirFiles[0]:dirFiles[1];
            //是否merge成功
            if(mergeFile.length()==chunk.getTotalSize()){
                //merge成功删除part目录 设置状态
                deleteDir(dir.getPath());
                this.mergeSuccess.set(true);
                this.partIndex.clear();
            }else if(partSize==chunk.getTotalChunks()){
                //删除merge文件重新merge
                mergeFile.delete();
                mergeFile(savePartDir,chunk,null);
            }else{
                //重新上传
                this.mergeSuccess.set(false);
                this.partIndex.clear();
                deleteDir(saveDir);
            }
        }
        //merge文件或目录
        if(dirSize==1){
            File targetFile = dirFiles[0];
            boolean isDirectory = targetFile.isDirectory();
            //目录是否重新merge
            if(isDirectory){
                if(partSize==chunk.getTotalChunks()){
                    mergeFile(savePartDir,chunk,null);
                }else{
                    for (File file : partFiles) {
                        String filename = file.getName();
                        int partIndex = Integer.parseInt(filename.substring(filename.lastIndexOf("_") + 1));
                        this.getPartIndex().add(partIndex);
                    }
                }
            }else{
                //文件是否merge成功
                if(targetFile.length()==chunk.getTotalSize()){
                    this.mergeSuccess.set(true);
                    this.partIndex.clear();;
                    this.mergeFile = targetFile;
                }else{
                    //重新上传
                    this.mergeSuccess.set(false);
                    this.partIndex.clear();
                    deleteDir(saveDir);
                }
            }

        }
        return this;
    }

    /**
     * 断点续传上传
     *
     * @param file      文件流字节数组
     * @param fileChunk 分片信息
     */
    public UploadResult uploadPart(byte[] file, FileChunk fileChunk) {
        Assert.isFalse(fileChunk == null, "上传失败：缺少文件分片信息！");
        String identifier = fileChunk.getIdentifier();
        Assert.isFalse(StrUtil.isBlank(identifier), "上传失败：分片信息缺少文件唯一标识(identifier)！");

        lastAccessTime = new Date();

        //频繁暂停开始 防止重复
        Integer chunkNumber = fileChunk.getChunkNumber();
        int storeTotalChunks = partIndex.size();
        Integer totalChunks = fileChunk.getTotalChunks();
        if (!partIndex.add(chunkNumber)||storeTotalChunks==totalChunks || mergeSuccess.get()) {
            return null;
        }

        //创建存放目录
        String savePartDir = UPLOAD_TEMP_DIR + (UPLOAD_TEMP_DIR.endsWith(File.separator) ? "" : File.separator) + identifier + File.separator + "parts";
        File partDir = new File(savePartDir);
        if (!partDir.exists()) {
            partDir.mkdirs();
        }

        String filename = fileChunk.getFilename();
        String targetFile = savePartDir + File.separator + String.format("%s_%s", filename, chunkNumber);
        FileUtil.writeBytes(file, targetFile);

        //FileChunk chunk = this.chunk.get();
        //chunk.setChunkNumber(currentNum);
        //merge
        if (partIndex.size() == totalChunks) {
            //移除当前任务
            //removeTask(fileChunk.getMd5Str());
            mergeFile(partDir.getPath(), fileChunk, null);
            //fileChunk.setRelativePath(tempDir + File.separator + filename);
        }
        return buildUploadResult(identifier, fileChunk);
    }

    private UploadResult buildUploadResult(String identifier, FileChunk chunk) {
        Integer totalChunks = chunk.getTotalChunks();
        Long totalSize = chunk.getTotalSize();
        String filename = chunk.getFilename();
        int chunkNumber = partIndex.size();
        double completionRate = new BigDecimal(chunkNumber).divide(new BigDecimal(totalChunks), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return this.new UploadResult(identifier, filename, totalChunks, totalSize, chunkNumber, completionRate);
    }

    /**
     * merge文件
     *
     * @param partPath  临时分片目录 xxx/parts
     * @param filename 合并后的文件名 不传入则=FileChunk.filename
     * @return
     */
    public void mergeFile(String partPath, FileChunk chunk, String filename) {
        Assert.isFalse(StrUtil.isBlank(partPath), "mergeFile：缺少目录信息！");
        File partDirFile = new File(partPath);
        Assert.isTrue(partDirFile.isDirectory(), "mergeFile：不是一个目录！");
        File[] listFiles = partDirFile.listFiles();
        Assert.isFalse(listFiles == null || listFiles.length == 0, "mergeFile：没有需要merge的文件！");
        ArrayList<File> files = new ArrayList<>(Arrays.asList(listFiles));
        //按照分片号排序
        files.sort((file1, file2) -> {
            String filename1 = file1.getName();
            String filename2 = file2.getName();
            Integer index1 = Integer.parseInt(filename1.substring(filename1.lastIndexOf("_") + 1));
            Integer index2 = Integer.parseInt(filename2.substring(filename2.lastIndexOf("_") + 1));
            return index1.compareTo(index2);
        });
        //开始merge
        //merge的目标文件
        filename = StrUtil.isBlank(filename) ? chunk.getFilename() : filename;
        File targetFile = new File(partDirFile.getParent() + File.separator + filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
        } catch (FileNotFoundException e) {
            throw new BusinessException("mergeFile:targetFile流构建失败！");
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        files.forEach(file -> {
            try (
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
            ) {
                byte[] bs = new byte[bis.available()];
                int read = bis.read(bs);
                if (read != -1) {
                    bos.write(bs);
                    bos.flush();
                }
            } catch (Exception e) {
                throw new BusinessException("merge文件异常：请联系管理员或重新上传！");
            }
        });
        try {
            bos.close();
            fos.close();
        } catch (IOException e) {
            throw new BusinessException("mergeFile:bos关闭异常！");
        }
        //merge成功删除分片目录并设置状态
        deleteDir(partPath);
        mergeFile = targetFile;
        mergeSuccess.set(true);
        this.partIndex.clear();
    }

    /**
     * 移除指定的任务
     *
     * @param identifier
     */
    private void removeTask(String identifier) {
        TASK_LIST.remove(identifier);
    }

    /**
     * 删除指定目录
     *
     * @param dirPath
     */
    private static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile()) {
                        f.delete();
                    } else {
                        deleteDir(f.getPath());
                    }
                }
            }
            dir.delete();
        }
    }

    @AllArgsConstructor
    @Data
    public class UploadResult implements Serializable {

        private static final long serialVersionUID = -6875257171242898931L;
        @ApiModelProperty("文件唯一标识")
        private String identifier;

        @ApiModelProperty("文件名")
        private String filename;

        @ApiModelProperty("总分片数")
        private Integer totalChunks;

        @ApiModelProperty("文件总大小")
        private Long totalSize;

        @ApiModelProperty("当前片数")
        private Integer chunkNumber;

        @ApiModelProperty("完成率")
        private Double completionRate;
    }

}
