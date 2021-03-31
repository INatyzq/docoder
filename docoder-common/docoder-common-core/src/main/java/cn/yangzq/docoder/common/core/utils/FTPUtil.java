package cn.yangzq.docoder.common.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.yangzq.docoder.common.core.exception.SystemException;
import cn.yangzq.docoder.common.core.enums.UploadStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
*@author yangzq
*@description FTP工具类
*/
@Slf4j
@ConditionalOnProperty({"file-server-ftp.host"})
@Component
public class FTPUtil {

    /**
     * FTP链接配置信息
     */
    @Value("${file-server-ftp.host}")
    private String HOST;
    @Value("${file-server-ftp.port}")
    private Integer PORT;
    @Value("${file-server-ftp.user-name}")
    private String USER_NAME;
    @Value("${file-server-ftp.password}")
    private String PASSWORD;

    private final static String CONTROL_ENCODING = "GBK";

    private final static int CONNECT_CODE = 230;

    /**
     * 上传操作
     */
    public String upload(InputStream inputStream, String path,String fileName){
        FTPClient ftp = null;
        try {
            //获取连接
            ftp = connect();
            //设置文件类型(二进制文件)
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //进入目标文件夹
            createAndEnterDir(ftp,path);
            //保存文件
            boolean isSuccess = ftp.storeFile(fileName, inputStream);
            if(!isSuccess){
                String msg = "FTP文件"+fileName+"上传失败！";
                log.error(msg);
                throw new SystemException(msg);
            }
            //关闭相关资源
            inputStream.close();
            path = ftp.printWorkingDirectory();
        } catch (IOException e) {
            log.error(this.getClass().getSimpleName(),e);
            throw new SystemException(e);
        }finally {
            //关闭相关资源
            if (ftp!=null && ftp.isConnected()) {
                try {
                    closeResource(ftp);
                } catch (IOException ioe) {
                    log.error(this.getClass().getSimpleName(),ioe);
                }
            }
        }
        return path+fileName;
    }

    /**
     * 文件断点续传
     */
    public UploadStatus breakpointUpload(String local, String remoteDir,String asFilename) {
        FTPClient ftp = null;
        try {
            //设置PassiveMode传输
            ftp = connect();
            ftp.enterLocalPassiveMode();
            //设置以二进制流的方式传输
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setControlEncoding("GBK");
            UploadStatus result;
            //对远程目录的处理
            String remoteFileName = local;
            if (remoteFileName.contains(File.separator)) {
                remoteFileName = remoteFileName.substring(remoteFileName.lastIndexOf(File.separator) + 1);
                if(StrUtil.isNotBlank(asFilename)){
                    remoteFileName = asFilename;
                }
                // 创建服务器远程目录结构，创建失败直接返回
                if (!createAndEnterDir(ftp,remoteDir)) {
                    return UploadStatus.Create_Directory_Fail;
                }
            }
            //检查远程是否存在文件
            FTPFile[] files = ftp.listFiles(new String(remoteFileName.getBytes("GBK"), "iso-8859-1"));
            if (files.length == 1) {
                long remoteSize = files[0].getSize();
                File f = new File(local);
                long localSize = f.length();
                if (remoteSize == localSize) {
                    return UploadStatus.File_Exits;
                } else if (remoteSize > localSize) {
                    return UploadStatus.Remote_Bigger_Local;
                }
                // 尝试移动文件内读取指针,实现断点续传
                result = uploading(ftp,f,remoteFileName,remoteSize);
                // 如果断点续传没有成功，则删除服务器上文件，重新上传
                if (result == UploadStatus.Upload_From_Break_Failed) {
                    if (!ftp.deleteFile(remoteFileName)) {
                        return UploadStatus.Delete_Remote_Failed;
                    }
                    result = uploading(ftp,f,remoteFileName,0);
                }
            } else {
                result = uploading(ftp,new File(local),remoteFileName,0);
            }
            closeResource(ftp);
            return result;
        } catch (IOException e) {
            log.error(this.getClass().getSimpleName(),e);
            throw new SystemException(e);
        }finally {
            //关闭相关资源
            if (ftp!=null && ftp.isConnected()) {
                try {
                    closeResource(ftp);
                } catch (IOException ioe) {
                    log.error(this.getClass().getSimpleName(),ioe);
                    throw new SystemException(ioe);
                }
            }
        }
    }

    /**
     * 断点续传操作
     */
    private UploadStatus uploading(FTPClient ftpClient,File localFile,String remoteFile,long remoteSize) throws IOException {
        UploadStatus status;
        // 显示进度的上传
        long step = localFile.length() / 100;
        long process = 0;
        long localreadbytes = 0L;
        RandomAccessFile raf = new RandomAccessFile(localFile, "r");
        OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"), "iso-8859-1"));
        // 断点续传
        if (remoteSize > 0) {
            ftpClient.setRestartOffset(remoteSize);
            process = remoteSize / step;
            raf.seek(remoteSize);
            localreadbytes = remoteSize;
        }
        byte[] bytes = new byte[5120];
        int c;
        try {
            while ((c = raf.read(bytes)) != -1) {
                out.write(bytes, 0, c);
                localreadbytes += c;
                if (localreadbytes / step != process) {
                    process = localreadbytes / step;
                    log.info("上传进度:" + process);
                }
            }
            out.flush();
        }catch (Exception e){
            throw e;
        }finally {
            if(raf!=null){
                raf.close();
            }
            if(out!=null){
                out.close();
            }
        }
        boolean result = ftpClient.completePendingCommand();
        if (remoteSize > 0) {
            status = result ? UploadStatus.Upload_From_Break_Success : UploadStatus.Upload_From_Break_Failed;
        } else {
            status = result ? UploadStatus.Upload_New_File_Success : UploadStatus.Upload_New_File_Failed;
        }
        return status;
    }

    /**
     * 删除文件
     */
    public boolean delete(String path,String fileName){
        FTPClient ftp = null;
        try {
            //获取连接
            ftp = connect();
            boolean isSuccess = deleteOpt(ftp, path, fileName);
            if(!isSuccess){
                log.error("文件["+fileName+"]删除失败！");
                throw new SystemException("文件["+fileName+"]删除失败！");
            }
            return true;
        } catch (IOException e) {
            log.error(this.getClass().getSimpleName(),e);
            throw new SystemException(e);
        }finally {
            //关闭相关资源
            if (ftp!=null && ftp.isConnected()) {
                try {
                    closeResource(ftp);
                } catch (IOException ioe) {
                    log.error(this.getClass().getSimpleName(),ioe);
                    throw new SystemException(ioe);
                }
            }
        }
    }

    /**
     *
     * @param pathNameMap
     * @return 文件路径和文件名字的键值对
     */
    public boolean deleteBatch(Map<String, List<String>> pathNameMap){
        FTPClient ftp = null;
        try {
            //获取连接
            ftp = connect();
            for(Map.Entry<String,List<String>> entry:pathNameMap.entrySet()){
                String dir = entry.getKey();
                List<String> fileNames = entry.getValue();
                if(CollectionUtil.isNotEmpty(fileNames)){
                    for(String fileName:fileNames){
                        boolean isSuccess = deleteOpt(ftp,entry.getKey(),fileName);
                        if(!isSuccess){
                            log.error("文件["+fileName+"]删除失败！");
                            throw new SystemException("文件["+fileName+"]删除失败！");
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            log.error(this.getClass().getSimpleName(),e);
            throw new SystemException(e);
        }finally {
            //关闭相关资源
            if (ftp!=null && ftp.isConnected()) {
                try {
                    closeResource(ftp);
                } catch (IOException ioe) {
                    log.error(this.getClass().getSimpleName(),ioe);
                }
            }
        }
    }

    /**
     * 实际删除操作
     */
    private boolean deleteOpt(FTPClient ftp,String path,String fileName) throws IOException {
        // 转移到FTP服务器目录
        ftp.changeWorkingDirectory(path);
        if(!"/".equals(path.substring(path.length()-1))){
            path = path+"/";
        }
        if(ftp.listFiles(fileName).length==0){
            return true;
        }
        return ftp.deleteFile(fileName);
    }

    /**
     * ftp链接目标服务器
     */
    private FTPClient connect() throws IOException {
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding(CONTROL_ENCODING);
        //连接和登录
        ftp.connect(HOST,PORT);
        ftp.login(USER_NAME,PASSWORD);
        //开启被动模式，否则文件上传不成功，也不报错
        ftp.enterLocalPassiveMode();
        if(ftp.getReplyCode()!=CONNECT_CODE){
            log.error("FTP服务器连接失败！");
            throw new SystemException("FTP服务器连接失败！");
        }
        return ftp;
    }

    /**
     * 切换到目标目录 不存在则创建
     */
    private boolean createAndEnterDir(FTPClient client,String path)throws IOException{
        String[] dirs = path.split("/");
        StringBuilder workPath = new StringBuilder("/");
        for(String dir:dirs){
            if(StrUtil.isBlank(dir)){
                continue;
            }
            workPath.append(dir);
            if(!client.changeWorkingDirectory(workPath.toString())){
                client.makeDirectory(workPath.toString());
            }
            client.changeWorkingDirectory(workPath.toString());
            workPath.append("/");
        }
        return true;
    }

    public void closeResource(FTPClient client) throws IOException {
        if(client.isConnected()){
            client.logout();
            client.disconnect();
        }
    }
}
