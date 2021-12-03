package cn.yangzq.docoder.common.security.util;

import cn.dev33.satoken.stp.StpUtil;
import java.util.Optional;

/**
*@author yangzq
*@description 用户上下文持有类
**/
public class IUserContextHolder extends StpUtil {

    private static final String USER_SESSION_KEY = "userInfo";

    /**
     * 存入用户信息到用户会话
     * @param userInfoJson 用户信息json
     */
    public static <T> void storeUserInPrincipal(String userInfoJson){
       StpUtil.getSession(true).set(USER_SESSION_KEY,userInfoJson);
    }

    /**
     * 存入用户信息到token会话
     * @param userInfoJson 用户信息json
     */
    public static void storeUserInToken(String userInfoJson){
        StpUtil.getTokenSession().set(USER_SESSION_KEY,userInfoJson);
    }

    /**
     * 获取当前用户
     * @return 用户实体
     */
    protected static String getCurrentUserJson(){
        return Optional.ofNullable(StpUtil.getTokenSession().get(USER_SESSION_KEY)).map(Object::toString).orElse(
                Optional.ofNullable(StpUtil.getSession().get(USER_SESSION_KEY)).map(Object::toString).orElse("")
        );
    }

}
