package cn.yangzq.docoder.user.mapper;

import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.user.vo.UserDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
*@author yangzq
*@description 系统用户Mapper
**/
public interface SysUserMapper extends BaseMapper<SysUser> {

    Pageable<SysUserVo> selectDataPage(@Param("param")SysUserVo user, @Param("page")Pageable<SysUserVo> page);

    Pageable<SysUserVo> selectRbacPage(@Param("param")RbacParam param, @Param("page")Pageable<PermissionDetail> page);

    UserDetailVO selectUserDetail(@Param("userId") Integer userId,@Param("userName") String userName);
}
