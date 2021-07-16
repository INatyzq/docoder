package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.SysRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
*@author yangzq
*@description 系统角色表 服务类
**/
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取角色分页列表
     * @param param
     * @param page
     * @return
     */
    Pageable<SysRoleVo> getPage(SysRoleVo param, Pageable<SysRoleVo> page);

    /**
     * 基于rbac的分页查询
     * @param param
     * @param page
     * @return
     */
    Pageable<SysRoleVo> getRbacPage(RbacParam param, Pageable<PermissionDetail> page);
}
