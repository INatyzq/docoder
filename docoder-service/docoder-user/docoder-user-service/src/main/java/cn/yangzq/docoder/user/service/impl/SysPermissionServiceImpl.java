package cn.yangzq.docoder.user.service.impl;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.service.SysPermissionService;
import cn.yangzq.docoder.user.vo.PermissionVO;
import cn.yangzq.docoder.user.mapper.SysPermissionMapper;
import cn.yangzq.docoder.user.maputil.PoToVoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
*@author yangzq
*@description 系统权限表 服务实现类
**/
@Transactional
@Component
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private PoToVoMapper poToVoMapper;

    @Override
    public List<PermissionVO> getUserPermissionList(Integer userId) {
        List<SysPermission> permissionList= permissionMapper.selectUserPermissionList(userId);
        return poToVoMapper.permission(permissionList);
    }
}
