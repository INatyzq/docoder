package cn.yangzq.docoder.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.mapper.SysUserMapper;
import cn.yangzq.docoder.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
*@author yangzq
*@description 系统用户表 服务实现类
**/
@Transactional
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public boolean isRepeat(UserRegisterForm form) {
        String userName = form.getUserName();
        String email = form.getEmail();
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(userName),"USER_NAME",userName);
        wrapper.or(StrUtil.isNotBlank(email));
        wrapper.eq(StrUtil.isNotBlank(email),"EMAIL",email);
        return userMapper.selectCount(wrapper)>0;
    }
}
