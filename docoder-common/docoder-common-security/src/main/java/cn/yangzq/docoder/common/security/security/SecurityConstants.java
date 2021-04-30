package cn.yangzq.docoder.common.security.security;

/**
*@author yangzq
*@description 安全模块常量
**/
public interface SecurityConstants {

    interface Header {

        String WEB_TOKEN = "web_token";

    }

    interface RedisKey {

        String AUTH_SCHIN_WEB = "AUTH:SCHIN:WEB:";

        String AUTH_SCHIN_APP = "AUTH:SCHIN:APP:";

        String AUTH_CLASSCARD_DEVICE = "AUTH:CLASSCARD:DEVICE:";

        String AUTH_CLASSCARD_APP = "AUTH:CLASSCARD:APP:";
    }

}
