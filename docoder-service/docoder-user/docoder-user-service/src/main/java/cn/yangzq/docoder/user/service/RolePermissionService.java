package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.RolePermission;
import cn.yangzq.docoder.user.form.RbacForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
*@author yangzq
*@description 角色权限关系表 服务类
**/
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 批量绑定角色和权限的关系
     * @param forms
     */
    void bindBatch(List<RbacForm> forms);
}
