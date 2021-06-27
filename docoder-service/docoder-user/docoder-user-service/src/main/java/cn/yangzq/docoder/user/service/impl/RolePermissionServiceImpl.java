package cn.yangzq.docoder.user.service.impl;

import cn.yangzq.docoder.user.entity.RolePermission;
import cn.yangzq.docoder.user.form.UserRoleForm;
import cn.yangzq.docoder.user.mapper.RolePermissionMapper;
import cn.yangzq.docoder.user.service.RolePermissionService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
*@author yangzq
*@description 角色权限关系表 服务实现类
**/
@Transactional
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void bindBatch(List<UserRoleForm> forms) {
        UpdateWrapper<RolePermission> wrapper = new UpdateWrapper<>();
        List<RolePermission> insertList = new ArrayList<>();
        forms.forEach(form->{
            Integer action = form.getAction();
            Integer roleId = form.getRoleId();
            Integer permissionId = form.getPermissionId();
            if(action==1){
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                insertList.add(rp);
            }else{
                wrapper.eq("ROLE_ID",roleId);
                wrapper.eq("PERMISSION_ID",permissionId);
                rolePermissionMapper.delete(wrapper);
            }
        });
        if(insertList.size()>0){
            saveBatch(insertList);
        }
    }
}
