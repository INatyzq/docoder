package cn.yangzq.docoder.base.maputil;

import cn.yangzq.docoder.base.entity.SysAttachment;
import cn.yangzq.docoder.base.vo.AttachmentVo;
import org.mapstruct.Mapper;

/**
*@author yangzq
*@description PO转VO mapper
**/
@Mapper(componentModel = "spring")
public interface PoToVoMapper {

    AttachmentVo toAttachmentVo(SysAttachment attachment);

}
