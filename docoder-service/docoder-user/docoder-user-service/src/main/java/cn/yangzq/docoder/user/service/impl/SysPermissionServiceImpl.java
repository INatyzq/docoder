package cn.yangzq.docoder.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysPermissionService;
import cn.yangzq.docoder.user.mapper.SysPermissionMapper;
import cn.yangzq.docoder.user.maputil.PoToVoMapper;
import cn.yangzq.docoder.user.vo.RbacVo;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    public List<SysPermissionVo> getUserPermissionList(Integer userId) {
        List<SysPermission> permissionList= permissionMapper.selectUserPermissionList(userId);
        return poToVoMapper.permissionList(permissionList);
    }

    @Override
    public RbacVo getRbac(RbacParam param) {
        RbacVo vo = new RbacVo();

        Set<String> idsStrList = permissionMapper.selectRbac(param);

        Set<String> userIds = vo.getUserIds();
        Set<String> roleIds = vo.getRoleIds();
        Set<String> permissionIds = vo.getPermissionIds();
        idsStrList.forEach(idsStr->{
            if(StrUtil.isNotBlank(idsStr)){
                String[] ids = idsStr.split("_");
                userIds.add(StrUtil.blankToDefault(ids[0],null));
                roleIds.add(StrUtil.blankToDefault(ids[1],null));
                permissionIds.add(StrUtil.blankToDefault(ids[2],null));
            }
        });
        return vo;
    }
}
