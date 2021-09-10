package cn.yangzq.docoder.security.server.handler;

import cn.yangzq.docoder.common.core.exception.AuthException;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.security.server.config.WebSecurityConfig;
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
    @Autowired
    private AbstractUserContextHolder userContextHolder;
    @Autowired
    private WebSecurityConfig securityConfig;

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
            boolean needSuccess = authentication.isNeedSuccess();
            if(authentication.allowAccess(request)) {
                try {
                    authentication.accessIntercept(request, response, redisUtil, securityConfig);
                }catch (Exception e){
                    if(needSuccess){
                       throw e;
                    }
                }
                isAuth = true;
            }else{
                isAuth = !needSuccess;
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
