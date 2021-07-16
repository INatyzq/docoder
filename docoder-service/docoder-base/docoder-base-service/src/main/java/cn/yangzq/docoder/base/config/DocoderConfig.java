package cn.yangzq.docoder.base.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
*@author yangzq
*@description 工程配置
**/
@ApiModel("工程配置")
@Data
@ConfigurationProperties(prefix = "schinweb")
@Component
public class DocoderConfig {

    @ApiModelProperty("token失效时间")
    private long tokenTimeSecond;

    @ApiModelProperty("认证接口url")
    private String dispatcherAuthenticationUrl;

    @ApiModelProperty("token请求头key")
    private String tokenHeaderKey;

    @ApiModelProperty("缓存key前缀")
    private String cacheKeyPrefix;

    @ApiModelProperty("token认证url")
    private String ssoUrl;

    @ApiModelProperty("上传临时文件目录")
    private String uploadTempDir;
}
