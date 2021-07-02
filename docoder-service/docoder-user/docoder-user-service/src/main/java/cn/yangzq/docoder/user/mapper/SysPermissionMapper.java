package cn.yangzq.docoder.user.mapper;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.PermissionTreeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
*@author yangzq
*@description 系统权限表 Mapper 接口
**/
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectUserPermissionList(Integer userId);

    Set<String> selectRbac(RbacParam param);

    List<PermissionTreeVo> selectTreeNode();
}
