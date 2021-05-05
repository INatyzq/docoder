package cn.yangzq.docoder.api.user.iservice;

import cn.yangzq.docoder.api.user.entity.SysPermission;
import cn.yangzq.docoder.api.user.vo.PermissionVO;
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
    List<PermissionVO> getUserPermissionList(Integer userId);

}
