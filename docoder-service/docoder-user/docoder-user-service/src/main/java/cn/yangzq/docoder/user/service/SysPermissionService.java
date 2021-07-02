package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.PermissionTreeVo;
import cn.yangzq.docoder.user.vo.RbacVo;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
*@author yangzq
*@description 系统权限表 服务类
**/
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 获取用户权限
     * @param userId
     * @return List<SysPermission>
     */
    List<SysPermissionVo> getUserPermissionList(Integer userId);

    /**
     * 获取rbac
     * @param param
     * @return
     */
    RbacVo getRbac(RbacParam param);

    /**
     * 获取树形数据
     * @return
     */
    List<PermissionTreeVo> getTree();
}
