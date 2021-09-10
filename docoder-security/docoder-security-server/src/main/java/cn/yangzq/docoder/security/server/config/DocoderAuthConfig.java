package cn.yangzq.docoder.security.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
*@author yangzq
*@description 认证配置
**/
@Data
@ConfigurationProperties(prefix = "docoder-auth")
@Component
public class DocoderAuthConfig {

    private String authHeaderKey;

    private long expireTimeMillisecond;

    private boolean isAllowMultipleTerminal;

    private boolean isAllowMultiplePrincipal;

}
