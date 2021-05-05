package cn.yangzq.docoder.base.service;


import cn.hutool.core.util.StrUtil;
import cn.yangzq.docoder.api.base.entity.SysAttachment;
import cn.yangzq.docoder.api.base.iservice.IUploadService;
import cn.yangzq.docoder.api.base.iservice.SysAttachmentService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
*@author yangzq
*@description 文件上传服务
*/
@Transactional
@DubboService
@Component
public class UploadServiceImpl implements IUploadService {

    @Autowired
    private SysAttachmentService attachmentService;

    @Override
    public Integer uploadAndSave(MultipartFile file,String savePath,Integer objId) {
        SysAttachment attachment = buildAttachment(file, savePath, objId);
        attachmentService.save(attachment);
        return attachment.getId();
    }

    private SysAttachment buildAttachment(MultipartFile file,String savePath,Integer objId){
        String filename = file.getOriginalFilename();
        String fileType = filename.split(".")[1];
        double fileSize = file.getSize()/1024D;
        SysAttachment attachment = new SysAttachment();
        attachment.setObjectId(objId);
        attachment.setFileName(filename);
        attachment.setFilePath(savePath);
        attachment.setFileType(fileType);
        attachment.setFileSize(fileSize);
        attachment.setStatus(1);
        return attachment;
    }
}
