package cn.yangzq.docoder.base.service;

import cn.yangzq.docoder.api.base.entity.SysAttachment;
import cn.yangzq.docoder.base.mapper.SysAttachmentMapper;
import cn.yangzq.docoder.api.base.iservice.SysAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
*@author yangzq
*@description 系统附件表 服务实现类
*/
@Transactional
@DubboService
@Component
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

}
