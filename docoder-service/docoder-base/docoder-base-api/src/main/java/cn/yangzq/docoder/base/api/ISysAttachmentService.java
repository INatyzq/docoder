package cn.yangzq.docoder.base.api;

import cn.yangzq.docoder.base.entity.po.SysAttachment;

/**
*@author yangzq
*@description 系统附件表 服务类
*/
public interface ISysAttachmentService {

    /**
     * 保存
     * @param attachment
     * @return
     */
    SysAttachment save(SysAttachment attachment);

}
