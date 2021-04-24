package cn.yangzq.docoder.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
*@author yangzq
*@description 工程配置
**/
@Data
@ConfigurationProperties(prefix = "docoder")
@Component
public class DocoderConfig {

    private long tokenTime;

    private long tokenRememberTime;

}
