package cn.yangzq.docoder.user.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.common.security.handler.IAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
*@author yangzq
*@description 安全访问认证器
**/
@Component
public class SecurityAuthenticator extends IAuthentication {

    @Autowired
    private DocoderConfig docoderConfig;

    @Override
    public boolean requireAuth(HttpServletRequest request, RedisUtil redisUtil) {
        return request.getRequestURI().endsWith(docoderConfig.getDispatcherAuthenticationUrl());
    }

    @Override
    public String dispatcherAuthenticationUrl() {
        return docoderConfig.getDispatcherAuthenticationUrl();
    }

    @Override
    public boolean allowAccess(HttpServletRequest request) {
        return StrUtil.isNotBlank(request.getHeader(docoderConfig.getTokenHeaderKey()));
    }

    @Override
    public void accessIntercept(HttpServletRequest request, HttpServletResponse response, RedisUtil redisUtil) {
        String headerVal = request.getHeader(docoderConfig.getTokenHeaderKey());
        String tokenCacheKey = docoderConfig.getWebCacheKeyToken()+headerVal;
        String token = redisUtil.get(tokenCacheKey);
        Assert.isFalse(StrUtil.isBlank(token),()->new BadCredentialsException("访问失败:token已失效,请重新认证。"));

        String[] tokenVal = token.split("_");
        String userId = tokenVal[0];
        String rememberMe = tokenVal[1];
        if("-1".equals(rememberMe)){
            redisUtil.delete(tokenCacheKey);
            throw new BadCredentialsException("访问失败:当前用户已在其他端点登录。");
        }

        long expireTime = "1".equals(rememberMe)?docoderConfig.getTokenTimeRememberSecond():docoderConfig.getTokenTimeSecond();
        redisUtil.expire(docoderConfig.getWebCacheKeyId()+userId,expireTime, TimeUnit.SECONDS);
        redisUtil.expire(tokenCacheKey,expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void setUserDetail(HttpServletRequest request, RedisUtil redisUtil,ThreadLocal<String> userDetailJsonStrLocal) {
        String headerVal = request.getHeader(docoderConfig.getTokenHeaderKey());
        String tokenCacheKey = docoderConfig.getWebCacheKeyToken()+headerVal;
        String token = redisUtil.get(tokenCacheKey);
        String[] tokenVal = token.split("_");
        String userId = tokenVal[0];
        userDetailJsonStrLocal.set(redisUtil.get(docoderConfig.getWebCacheKeyId() + userId));
    }
}
