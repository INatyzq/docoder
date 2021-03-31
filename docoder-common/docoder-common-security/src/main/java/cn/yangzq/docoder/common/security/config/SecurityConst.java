package cn.yangzq.docoder.common.security.config;

/**
*@author yangzq
*@description 安全类常量
**/
public interface SecurityConst {

    String ACCOUNT = "ACCOUNT";
    String ACCOUNT_ID = "ACCOUNT_ID";
    String LOGIN_REMEMBER_ME = "IS_REMEMBER_ME";
    String WEB_TOKEN_KEY = "AUTH:WEB:";

    /**
     * 过期时间，单位毫秒
     */
    int EXPIRE_TIME_JWT = -1;

    /**
     * 过期时间，单位毫秒
     */
    long EXPIRE_TIME = 24*60*60*1000;

    /**
     * 记住我 过期时间，单位毫秒
     */
    long EXPIRE_TIME_REMEMBER_ME = 7*24*60*60*1000;

    /**
     * 一天过期时间，单位毫秒
     */
    long EXPIRE_TIME_DAY = 24*60*60*1000;

    /**
     * 一天过期时间，单位毫秒
     */
    String SECRET_KEY = "docoder_secret_key";

}
