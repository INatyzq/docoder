package cn.yangzq.docoder.user.service.impl;

import cn.yangzq.docoder.user.entity.UserRole;
import cn.yangzq.docoder.user.form.UserRoleForm;
import cn.yangzq.docoder.user.mapper.UserRoleMapper;
import cn.yangzq.docoder.user.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
*@author yangzq
*@description 用户角色关系表 服务实现类
**/
@Transactional
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void bindBatch(List<UserRoleForm> forms) {
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
            }
        });
        if(insertList.size()>0){
            saveBatch(insertList);
        }
    }
}
