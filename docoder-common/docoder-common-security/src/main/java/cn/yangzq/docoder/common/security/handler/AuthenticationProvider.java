package cn.yangzq.docoder.common.security.handler;

import cn.yangzq.docoder.common.core.exception.AuthException;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
*@author yangzq
*@description 通过不同的请求头进行不同的方式认证
**/
@Slf4j
@Component
public class AuthenticationProvider {

    @Autowired
    private final List<IAuthentication> providerList = new ArrayList<>();
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 认证
     * @param request
     * @param response
     * @return 是否转发到认证接口
     */
    public boolean authentication(HttpServletRequest request, HttpServletResponse response){
        boolean isAuth = false;
        boolean isDispatcher = false;
        for(IAuthentication authentication:providerList){
            boolean requireDispatcherAuth = authentication.requireDispatcherAuth(request, redisUtil);
            if(authentication.allowAccess(request) && !requireDispatcherAuth){
                authentication.accessIntercept(request,response,redisUtil);
                isAuth = true;
                /*authentication.setUserDetail(request, redisUtil, userContextHolder.userDetailJsonStrLocal);*/
            }else{
                if(requireDispatcherAuth){
                    String dispatcherAuthenticationUrl = authentication.dispatcherAuthenticationUrl();
                    isDispatcher = true;
                    try {
                        request.getRequestDispatcher(dispatcherAuthenticationUrl).forward(request,response);
                    } catch (Exception e) {
                        String msg = "认证url转发失败："+dispatcherAuthenticationUrl;
                        log.error(msg,e);
                        throw new AuthException(msg);
                    }
                    isAuth = true;
                }
            }
        }
        if(!isAuth){
            throw new AuthException("拒绝访问：请携带有效凭证！");
        }
        return isDispatcher;
    }

    public List<IAuthentication> getProviderList() {
        return providerList;
    }
}
