package cn.yangzq.docoder.user.mapper;

import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import cn.yangzq.docoder.user.vo.SysRoleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
*@author yangzq
*@description 系统角色表 Mapper 接口
**/
public interface SysRoleMapper extends BaseMapper<SysRole> {

    Pageable<SysRoleVo> selectDataPage(@Param("params")SysRoleVo params, @Param("page")Pageable<SysRoleVo> page);

    Pageable<SysRoleVo> selectRbacPage(@Param("param")RbacParam param, @Param("page")Pageable<SysPermissionVo> page);
}
