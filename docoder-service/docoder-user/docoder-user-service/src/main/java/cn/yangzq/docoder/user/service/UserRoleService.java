package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.UserRole;
import cn.yangzq.docoder.user.form.RbacForm;
import cn.yangzq.docoder.user.vo.RbacVo;
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
    void bindBatch(List<RbacForm> forms);

    /**
     * 批量绑定用户和角色和权限的关系
     * @param rbac
     */
    void bindAll(RbacVo rbac);
}
