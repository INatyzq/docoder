package cn.yangzq.docoder.common.security.handler;

import cn.yangzq.docoder.common.core.utils.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*@author yangzq
*@description 认证接口
**/
public abstract class IAuthentication {

    /**
     * 什么时候要求去认证
     * @param request
     * @param redisUtil
     * @return
     */
    public abstract boolean requireAuth(HttpServletRequest request,RedisUtil redisUtil);

    /**
     * 返回转发的认证url
     * @return 转发的认证url
     */
    public abstract String dispatcherAuthenticationUrl();

    /**
     * 是否允许访问
     * @param request
     * @return
     */
    public abstract boolean allowAccess(HttpServletRequest request);

    /**
     * 访问拦截
     * @param request
     * @param response
     * @param redisUtil
     */
    public abstract void accessIntercept(HttpServletRequest request, HttpServletResponse response,RedisUtil redisUtil);

    /**
     * 获取用户详情
     * @param request
     * @param redisUtil
     * @param userDetailJsonStrLocal
     * @return 用户详情
     */
    public abstract void setUserDetail(HttpServletRequest request,RedisUtil redisUtil,ThreadLocal<String> userDetailJsonStrLocal);

}
