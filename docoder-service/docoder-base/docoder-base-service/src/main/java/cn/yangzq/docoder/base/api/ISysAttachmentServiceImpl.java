package cn.yangzq.docoder.base.api;

import cn.yangzq.docoder.base.entity.po.SysAttachment;
import cn.yangzq.docoder.base.service.SysAttachmentService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
*@author yangzq
*@description 系统附件表 服务实现类
*/
@Transactional
@DubboService
@Component
public class ISysAttachmentServiceImpl implements ISysAttachmentService {

    @Autowired
    private SysAttachmentService attachmentService;

    @Override
    public SysAttachment save(SysAttachment attachment) {
        attachmentService.save(attachment);
        return attachment;
    }
}
