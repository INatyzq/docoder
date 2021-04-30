package cn.yangzq.docoder.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.core.exception.AuthException;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.common.security.security.JWTUtil;
import cn.yangzq.docoder.user.config.DocoderConfig;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.entity.UserDetail;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.mapper.SysUserMapper;
import cn.yangzq.docoder.user.service.SysPermissionService;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.user.vo.PermissionVO;
import cn.yangzq.docoder.user.vo.UserDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
*@author yangzq
*@description 系统用户表 服务实现类
**/
@Transactional
@DubboService
@Component
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private DocoderConfig docoderConfig;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysPermissionService permissionService;

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

    @Override
    public UserDetailVO login(UserLoginForm form) {
        String userName = form.getUserName();
        String userPwd = form.getUserPwd();
        userPwd = SecureUtil.md5(userPwd);

        //获取jwt
        long oneDaySecond = 24*60*60;
        String jwtToken = JWTUtil.sign(userName, userPwd, oneDaySecond);

        //验证用户
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_NAME",userName);
        wrapper.eq("STATUS",1);
        SysUser user = userMapper.selectOne(wrapper);
        JSONObject msg = JSONUtil.createObj();
        if(user==null){
            msg.set("userName",userName+":此账户不存在！");
            throw new AuthException(msg.toString(),true);
        }

        if(!user.getUserPwd().equals(userPwd)){
            msg.set("userPwd","密码不正确！");
            throw new AuthException(msg.toString(),true);
        }


        String webCacheKeyId = docoderConfig.getWebCacheKeyId();
        String webCacheKeyToken = docoderConfig.getWebCacheKeyToken();

        boolean rememberMe = form.isRememberMe();
        long expireTime = rememberMe?docoderConfig.getTokenTimeRememberSecond():docoderConfig.getTokenTimeSecond();
        //踢掉其他端点登录的用户
        String userStr = redisUtil.get(webCacheKeyId + userName);
        if(StrUtil.isNotBlank(userStr)){
            UserDetail cache = JSONUtil.toBean(userStr, UserDetail.class);
            String tokenKey = cache.getToken();
            redisUtil.setEx(webCacheKeyToken+tokenKey,userName+"_-1",expireTime,TimeUnit.SECONDS);
        }

        List<PermissionVO> permissionList = permissionService.getUserPermissionList(user.getId());

        //缓存数据
        UserDetail userDetail = BeanUtil.copyProperties(user, UserDetail.class);
        userDetail.setToken(jwtToken);
        userDetail.setPermissionList(permissionList);

        String tokenVal = userName+"_"+(rememberMe?1:0)+"_"+System.currentTimeMillis();
        redisUtil.setEx(webCacheKeyId+userName,JSONUtil.toJsonStr(userDetail),expireTime, TimeUnit.SECONDS);
        redisUtil.setEx(webCacheKeyToken+jwtToken,tokenVal,expireTime, TimeUnit.SECONDS);

        UserDetailVO detailVO = new UserDetailVO();
        detailVO.setUserName(userName);
        detailVO.setToken(jwtToken);
        detailVO.setPermissionList(permissionList);
        return detailVO;
    }

    @Override
    public Pageable<SysUser> getUserPage(SysUser user, Pageable<SysUser> page) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        String userName = user.getUserName();
        String nickname = user.getNickname();
        String email = user.getEmail();
        String phone = user.getPhone();
        String fullName = user.getFullName();
        wrapper.like(StrUtil.isNotBlank(userName),"user_name",userName);
        wrapper.like(StrUtil.isNotBlank(nickname),"nickname",nickname);
        wrapper.like(StrUtil.isNotBlank(email),"email",email);
        wrapper.like(StrUtil.isNotBlank(phone),"phone",phone);
        wrapper.like(StrUtil.isNotBlank(fullName),"full_name",fullName);
        return userMapper.selectPage(page,wrapper);
    }
}
