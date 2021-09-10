package cn.yangzq.docoder.security.server.handler;

import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.security.server.config.WebSecurityConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
*@author yangzq
*@description 认证接口
**/
public abstract class IAuthentication {


    /**
     * 此认证拦截器是否必须成功
     * @return
     */
    public abstract boolean isNeedSuccess();


    /**
     * 是否允许访问
     * @param request
     * @return
     */
    public abstract boolean allowAccess(HttpServletRequest request);

    /**
     * 内部访问拦截
     * @param request 请求
     * @param response 响应
     * @param redisUtil redis工具类
     */
    void accessIntercept(HttpServletRequest request, HttpServletResponse response, RedisUtil redisUtil, WebSecurityConfig securityConfig){

    }



    /**
     * 返回不需要认证的路径数组
     * @return
     */
    public abstract List<String> webIgnoring();
}
