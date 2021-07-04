package cn.yangzq.docoder.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.maputil.VoToVoMapper;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.user.vo.UserAuthDetailVO;
import cn.yangzq.docoder.common.core.exception.AuthException;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.common.security.security.JWTUtil;
import cn.yangzq.docoder.user.config.DocoderConfig;
import cn.yangzq.docoder.user.mapper.SysUserMapper;
import cn.yangzq.docoder.user.vo.UserDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
*@author yangzq
*@description 系统用户表 服务实现类
**/
@Transactional
@Component
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private DocoderConfig docoderConfig;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private VoToVoMapper voToVoMapper;

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
    public UserAuthDetailVO login(UserLoginForm form) {
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
        redisUtil.delete(webCacheKeyId + userName);
         /*String userStr = redisUtil.get(webCacheKeyId + userName);
        if(StrUtil.isNotBlank(userStr)){
           UserDetail cache = JSONUtil.toBean(userStr, UserDetail.class);
            String tokenKey = cache.getToken();
            redisUtil.setEx(webCacheKeyToken+tokenKey,userName+"_-1",expireTime,TimeUnit.SECONDS);
        }*/

        //缓存数据
        UserDetailVO userDetail = userMapper.selectUserDetail(user.getId(), null);
        UserAuthDetailVO detailVO = voToVoMapper.userDetail(userDetail);
        detailVO.setToken(jwtToken);
        detailVO.setRememberMe(rememberMe);

        Integer id = user.getId();
        String tokenVal = id+"_"+(rememberMe?1:0)+"_"+System.currentTimeMillis();
        redisUtil.setEx(webCacheKeyId+id,JSONUtil.toJsonStr(detailVO),expireTime, TimeUnit.SECONDS);
        redisUtil.setEx(webCacheKeyToken+jwtToken,tokenVal,expireTime, TimeUnit.SECONDS);

        return detailVO;
    }

    @Override
    public UserAuthDetailVO refresh(Integer userId){
        String userStr = redisUtil.get(docoderConfig.getWebCacheKeyId() + userId);
        if(StrUtil.isBlank(userStr)){
            throw new AuthException("刷新失败：凭证失效！");
        }
        UserAuthDetailVO detailVO = JSONUtil.toBean(userStr, UserAuthDetailVO.class);

        boolean rememberMe = detailVO.getRememberMe();
        long expireTime = rememberMe?docoderConfig.getTokenTimeRememberSecond():docoderConfig.getTokenTimeSecond();
        Integer id = detailVO.getId();
        String tokenVal = id+"_"+(rememberMe?1:0)+"_"+System.currentTimeMillis();
        redisUtil.setEx(docoderConfig.getWebCacheKeyId()+id,JSONUtil.toJsonStr(detailVO),expireTime, TimeUnit.SECONDS);
        redisUtil.setEx(docoderConfig.getWebCacheKeyToken()+detailVO.getToken(),tokenVal,expireTime, TimeUnit.SECONDS);
        return detailVO;
    }

    @Override
    public Pageable<SysUserVo> getUserPage(SysUserVo param, Pageable<SysUserVo> page) {
        return userMapper.selectDataPage(param,page);
    }

    @Override
    public UserDetailVO getUserDetail(Integer userId) {
        return userMapper.selectUserDetail(userId,null);
    }

    @Override
    public UserDetailVO getUserDetail(String userName) {
        return userMapper.selectUserDetail(null,userName);
    }

    @Override
    public Pageable<SysUserVo> getRbacPage(RbacParam param, Pageable<SysPermissionVo> page) {
        return userMapper.selectRbacPage(param,page);
    }

    @Override
    public void updateStatus(List<Integer> ids,Integer status) {
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
        wrapper.set("STATUS",status);
        wrapper.in("id",ids);
        userMapper.update(null,wrapper);
    }
}
