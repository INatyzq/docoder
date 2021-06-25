package cn.yangzq.docoder.user.maputil;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.SysPermissionForm;
import cn.yangzq.docoder.user.form.SysRoleForm;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
*@author yangzq
*@description 表单到PO的转换Mapper
**/
@Mapper(componentModel = "spring")
public interface FormToPoMapper {

    SysUser loginForm(UserLoginForm loginForm);

    SysUser registerForm(UserRegisterForm registerForm);

    List<SysRole> roleListForm(List<SysRoleForm> form);

    SysPermission sysPermissionForm(SysPermissionForm form);
}
