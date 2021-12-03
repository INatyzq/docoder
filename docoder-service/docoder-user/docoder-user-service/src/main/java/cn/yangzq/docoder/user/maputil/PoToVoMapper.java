package cn.yangzq.docoder.user.maputil;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.vo.SysRoleVo;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.user.vo.UserDetailVo;
import org.mapstruct.Mapper;

import java.util.List;

/**
*@author yangzq
*@description PO转VO mapper
**/
@Mapper(componentModel = "spring")
public interface PoToVoMapper {

    List<PermissionDetail> toPermissionDetailList(List<SysPermission> permissions);

    UserDetailVo toUserDetailVo(SysUser sysUser);

    SysUserVo toSysUserVo(SysUser sysUser);

    List<SysRoleVo> toSysRoleVoList(List<SysRole> roles);

}
