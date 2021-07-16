package cn.yangzq.docoder.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.yangzq.docoder.common.core.exception.BusinessException;
import cn.yangzq.docoder.user.entity.RolePermission;
import cn.yangzq.docoder.user.entity.UserRole;
import cn.yangzq.docoder.user.form.RbacForm;
import cn.yangzq.docoder.user.mapper.UserRoleMapper;
import cn.yangzq.docoder.user.service.RolePermissionService;
import cn.yangzq.docoder.user.service.UserRoleService;
import cn.yangzq.docoder.user.vo.RbacVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
*@author yangzq
*@description 用户角色关系表 服务实现类
**/
@Transactional
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public void bindBatch(List<RbacForm> forms) {
        UpdateWrapper<UserRole> wrapper = new UpdateWrapper<>();
        List<UserRole> insertList = new ArrayList<>();
        forms.forEach(form->{
            Integer action = form.getAction();
            Integer userId = form.getUserId();
            Integer roleId = form.getRoleId();
            if(action==1){
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                insertList.add(ur);
            }else{
                wrapper.eq("USER_ID",userId);
                wrapper.eq("ROLE_ID",roleId);
                userRoleMapper.delete(wrapper);
                wrapper.clear();
            }
        });
        if(insertList.size()>0){
            saveBatch(insertList);
        }
    }

    @Override
    public void bindAll(RbacVo rbac) {
        Set<Integer> userIds = rbac.getUserIds();
        Set<Integer> roleIds = rbac.getRoleIds();
        Set<Integer> permissionIds = rbac.getPermissionIds();

        String flag = (CollectionUtil.isNotEmpty(userIds)?"a":"")+(CollectionUtil.isNotEmpty(roleIds)?"b":"")+(CollectionUtil.isNotEmpty(permissionIds)?"c":"");
        if(StrUtil.isBlank(flag)){
            return;
        }
        if("ac".equals(flag)){
            throw new BusinessException("不允许直接给用户分配权限！",true);
        }

        List<UserRole> userRoles = new ArrayList<>();
        List<RolePermission> rolePermissions = new ArrayList<>();

        userIds.forEach(userId->{
            roleIds.forEach(roleId->{
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoles.add(ur);
            });
        });

        roleIds.forEach(roleId->{
            permissionIds.forEach(permissionId->{
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissions.add(rp);
            });
        });

        //清除
        UpdateWrapper<UserRole> deleteUR = new UpdateWrapper<>();
        deleteUR.in("USER_ID",userIds);
        userRoleMapper.delete(deleteUR);
        UpdateWrapper<RolePermission> deleteRP = new UpdateWrapper<>();
        deleteUR.in("ROLE_ID",roleIds);
        rolePermissionService.remove(deleteRP);
        //保存
        if(userRoles.size()>0){
            this.saveBatch(userRoles);
        }
        if(rolePermissions.size()>0){
            rolePermissionService.saveBatch(rolePermissions);
        }
    }
}
