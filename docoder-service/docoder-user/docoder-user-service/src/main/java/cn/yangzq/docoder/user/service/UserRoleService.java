package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.UserRole;
import cn.yangzq.docoder.user.form.UserRoleForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
*@author yangzq
*@description 用户角色关系表 服务类
**/
public interface UserRoleService extends IService<UserRole> {

    /**
     * 批量绑定用户和角色的关系
     * @param forms
     */
    void bindBatch(List<UserRoleForm> forms);
}
