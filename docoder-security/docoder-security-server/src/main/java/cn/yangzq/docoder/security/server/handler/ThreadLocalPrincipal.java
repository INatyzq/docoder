package cn.yangzq.docoder.security.server.handler;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
*@author yangzq
*@description 本地线程认证主体
**/
@Data
public class ThreadLocalPrincipal {

    private final ThreadLocal<String> authPrincipal = new ThreadLocal<>();

    private final Map<String,ThreadLocal<String>> authPrincipalMap = new HashMap<>();

    public ThreadLocal<String> newThreadLocalPrincipal(String authPrincipalKey){
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        authPrincipalMap.put(authPrincipalKey,threadLocal);
        return threadLocal;
    }

}
