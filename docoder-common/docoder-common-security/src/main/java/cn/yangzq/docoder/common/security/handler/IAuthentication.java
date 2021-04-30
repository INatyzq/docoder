package cn.yangzq.docoder.common.security.handler;

import cn.yangzq.docoder.common.core.utils.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*@author yangzq
*@description 认证接口
**/
public interface IAuthentication {

    /**
     * 什么时候要求去认证
     * @param request
     * @param redisUtil
     * @return
     */
    boolean requireAuth(HttpServletRequest request,RedisUtil redisUtil);

    /**
     * 返回转发的认证url
     * @return 转发的认证url
     */
    String dispatcherAuthenticationUrl();

    /**
     * 是否允许访问
     * @param request
     * @return
     */
    boolean allowAccess(HttpServletRequest request);

    /**
     * 访问拦截
     * @param request
     * @param response
     * @param redisUtil
     */
    void accessIntercept(HttpServletRequest request, HttpServletResponse response,RedisUtil redisUtil);


}
