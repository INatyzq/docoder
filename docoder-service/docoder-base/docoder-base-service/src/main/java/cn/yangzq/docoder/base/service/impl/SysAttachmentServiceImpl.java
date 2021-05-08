package cn.yangzq.docoder.base.service.impl;

import cn.yangzq.docoder.base.entity.po.SysAttachment;
import cn.yangzq.docoder.base.mapper.SysAttachmentMapper;
import cn.yangzq.docoder.base.service.SysAttachmentService;
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
@Service
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

}
