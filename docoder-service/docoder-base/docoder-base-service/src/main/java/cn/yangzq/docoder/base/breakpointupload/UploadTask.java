package cn.yangzq.docoder.base.breakpointupload;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.base.common.FilePathEnum;
import cn.yangzq.docoder.base.config.DocoderConfig;
import cn.yangzq.docoder.base.entity.SysAttachment;
import cn.yangzq.docoder.base.maputil.PoToVoMapper;
import cn.yangzq.docoder.base.service.SysAttachmentService;
import cn.yangzq.docoder.base.vo.AttachmentVo;
import cn.yangzq.docoder.common.core.enums.UploadStatus;
import cn.yangzq.docoder.common.core.exception.BusinessException;
import cn.yangzq.docoder.common.core.exception.NoSupportFileTypeException;
import cn.yangzq.docoder.common.core.utils.FTPUtil;
import cn.yangzq.docoder.common.core.utils.FileType;
import cn.yangzq.docoder.common.core.utils.FileTypeUtil;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.api.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author yangzq
 * @description 上传文件的异步任务
 **/
@Slf4j
@Component
public class UploadTask {

    //@Autowired
    private FTPUtil ftpUtil = new FTPUtil();
    @Autowired
    private SysAttachmentService attachmentService;
    @DubboReference
    private IUserService userService;
    @Autowired
    private DocoderConfig config;
    @Autowired
    private PoToVoMapper poToVoMapper;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private UploaderPlusUtil uploaderPlusUtil;

    /**
     * 单文件上传-业务类型
     *
     * @param requestAttributes
     * @param type
     * @param file
     * @return
     * @throws IOException
     */
    @Async
    public Future<ResultVo<SysAttachment>> uploadForBusType(RequestAttributes requestAttributes, String type, MultipartFile file) throws IOException {
        RequestContextHolder.setRequestAttributes(requestAttributes);
        if (file == null) {
            throw new BusinessException("上传失败，文件为空");
        }
        type = StrUtil.blankToDefault(type,"default");
        FilePathEnum filePathEnum = FilePathEnum.getInstance(type);
        String fileType = filePathEnum.getFileType();
        if (!FileType.ALL.equals(fileType)) {
            Set<String> categorySet = FileTypeUtil.getType(file.getInputStream());
            Assert.notEmpty(categorySet,()->new NoSupportFileTypeException("未识别的文件类型！"));
            if (!categorySet.contains(fileType)) {
                throw new NoSupportFileTypeException("请上传" + fileType + "类型的文件!");
            }
        }
        SysAttachment attachment = filePathEnum.buildAttachment(userService.getUserDetail().getId(), file.getOriginalFilename(), file.getSize());

        String filePath = attachment.getFilePath();
        String path = filePath.substring(filePath.indexOf(filePathEnum.getBasePath()));
        path = path.substring(0, path.indexOf(attachment.getFileName()));
        String fileName = attachment.getFileName();
        ftpUtil.upload(file.getInputStream(), path, fileName);
        try {
            attachmentService.save(attachment);
        } catch (Exception e) {
            ftpUtil.delete(path, fileName);
        }
        return AsyncResult.forValue(ResultVo.success(attachment));
    }

    /**
     * 断点续传前获取上传信息-业务类型
     *
     * @param requestAttributes
     * @param type
     * @param chunk
     * @return
     */
    @Async
    public Future<String> breakpointUploadBeforeForBusType(RequestAttributes requestAttributes, String type, FileChunk chunk) {
        RequestContextHolder.setRequestAttributes(requestAttributes);
        JSONObject result = JSONUtil.createObj();
        JSONObject data = JSONUtil.createObj();

        //获取上传的状态
        UploaderPlusUtil task = uploaderPlusUtil.initTask(chunk);
        UploaderPlusUtil uploadStatus = task.getUploadStatus(chunk);
        boolean isMergeSuccess = uploadStatus.getMergeSuccess().get();
        //merge成功
        if (isMergeSuccess) {
            //上传FTP和保存附件信息
            //保存附件并上传ftp
            SysAttachment attachment = saveAttachment(task, FilePathEnum.getInstance(type));
            //上传到FTP
            File mergeFile = task.getMergeFile();
            Assert.isFalse(mergeFile==null,"upload-part:没有获取到本地资源文件");
            uploadFTP(mergeFile.getPath(), attachment, FilePathEnum.getInstance(type));
            result.set("skipUpload", true);
            data.set("fileId", attachment.getId());
            result.set("data", data);
            task.getTaskCompletion().set(true);
        }else{
            result.set("uploaded", uploadStatus.getPartIndex());
        }
        result.set("success", true);
        return AsyncResult.forValue(result.toString());
    }

    /**
     * 断点续传分片上传-业务类型
     *
     * @param requestAttributes
     * @param type
     * @param chunk
     * @param file
     * @return
     * @throws IOException
     */
    @Async
    public Future<ResultVo<Object>> breakpointUploadForBusType(RequestAttributes requestAttributes, String type, FileChunk chunk, MultipartFile file) throws IOException {
        RequestContextHolder.setRequestAttributes(requestAttributes);

        if (file == null) {
            return new AsyncResult<>(ResultVo.failed().message("上传出错：文件对象为空"));
        }

        type = StrUtil.blankToDefault(type,"default");
        //校验上传的格式
        FilePathEnum filePathEnum = FilePathEnum.getInstance(type);
        String fileType = filePathEnum.getFileType();
        if (!FileType.ALL.equals(fileType) && chunk.getChunkNumber() == 1) {
            Set<String> categorySet = FileTypeUtil.getType(file.getInputStream());
            Assert.notEmpty(categorySet,()->new NoSupportFileTypeException("未识别的文件类型！"));
            if (!categorySet.contains(fileType)) {
                String msg = "请上传" + fileType + "类型的文件!";
                ResultVo<Object> result = ResultVo.failed().message(msg);
                return AsyncResult.forValue(result);
            }
        }

        //上传分片
        UploaderPlusUtil task = uploaderPlusUtil.initTask(chunk);
        UploaderPlusUtil.UploadResult result = task.uploadPart(file.getBytes(), chunk);

        //merge成功
        if (task.getMergeSuccess().get() && !task.getTaskCompletion().get()) {
            task.getTaskCompletion().set(true);
            //保存附件并上传ftp
            SysAttachment attachment = saveAttachment(task, filePathEnum);
            AttachmentVo attachmentVO = poToVoMapper.toAttachmentVo(attachment);
            task.getTaskData().set(attachmentVO);
            //上传到FTP
            File mergeFile = task.getMergeFile();
            Assert.isFalse(mergeFile==null,"upload-part:没有获取到本地资源文件");
            uploadFTP(mergeFile.getPath(), attachment, filePathEnum);
            return new AsyncResult<>(ResultVo.success(attachmentVO));
        }
        Object resultData = result;
        if(resultData==null){
            resultData = task.getTaskData().get();
        }
        return AsyncResult.forValue(ResultVo.success().data(resultData));
    }

    /**
     * 保存附件
     *
     * @author yangzq
     * @description
     **/
    private SysAttachment saveAttachment(UploaderPlusUtil task, FilePathEnum filePathEnum) {
        //构建附件信息
        Integer userId = userService.getUserDetail().getId();
        File mergeFile = task.getMergeFile();
        Assert.isFalse(mergeFile==null,"upload-saveAttachment:没有获取到本地资源文件");
        SysAttachment attachment = filePathEnum.buildAttachment(userId, mergeFile.getName(), mergeFile.length());
        //保存附件
        attachmentService.save(attachment);
        return attachment;
    }

    /**
     * 上传ftp
     *
     * @param fileLocalPath
     * @param attachment
     * @param filePathEnum
     * @return String ftp目录
     */
    private void uploadFTP(String fileLocalPath, SysAttachment attachment, FilePathEnum filePathEnum) {
        Assert.isTrue(new File(fileLocalPath).exists(),"uploadFTP：没有找到本地资源文件！");
        //taskExecutor.execute(()->{
            String filePath = attachment.getFilePath();
            String remoteDir = filePath.substring(filePath.indexOf(filePathEnum.getBasePath()));
            remoteDir = remoteDir.substring(0, remoteDir.lastIndexOf(attachment.getFileName()));
            String fileName = attachment.getFileName();
            UploadStatus uploadStatus = null;
            Integer fileId = attachment.getId();
            try {
                uploadStatus = ftpUtil.breakpointUpload(fileLocalPath, remoteDir, fileName);
                if(!UploadStatus.Upload_From_Break_Success.equals(uploadStatus) && !UploadStatus.Upload_New_File_Success.equals(uploadStatus)){
                    attachmentService.removeById(fileId);
                }
            }catch (Exception e){
                attachmentService.removeById(fileId);
                throw new BusinessException("文件FTP上传失败：请联系管理员或重新上传！");
            }
        //});
    }

}
