package cn.yangzq.docoder.user.maputil;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import cn.yangzq.docoder.user.vo.SysRoleVo;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.user.vo.UserAuthDetailVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
*@author yangzq
*@description PO转VO mapper
**/
@Mapper(componentModel = "spring")
public interface PoToVoMapper {

    List<SysPermissionVo> permissionList(List<SysPermission> permissions);

    UserAuthDetailVO UserAuthDetailVO(SysUser sysUser);

    SysUserVo userVo(SysUser sysUser);

    List<SysRoleVo> roleList(List<SysRole> roles);

}
