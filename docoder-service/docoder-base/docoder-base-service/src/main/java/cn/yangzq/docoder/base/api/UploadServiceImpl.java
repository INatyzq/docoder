package cn.yangzq.docoder.base.api;

import cn.yangzq.docoder.base.entity.po.IAttachment;
import cn.yangzq.docoder.base.service.SysAttachmentService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    private IAttachment buildAttachment(MultipartFile file, String savePath, Integer objId){
        String filename = file.getOriginalFilename();
        String fileType = filename.split(".")[1];
        double fileSize = file.getSize()/1024D;
        IAttachment attachment = new IAttachment();
        attachment.setObjectId(objId);
        attachment.setFileName(filename);
        attachment.setFilePath(savePath);
        attachment.setFileType(fileType);
        attachment.setFileSize(fileSize);
        attachment.setStatus(1);
        return attachment;
    }
}
