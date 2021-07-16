package cn.yangzq.docoder.content.config;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
*@author yangzq
*@description 工程配置
**/
@ApiModel("工程配置")
@Data
@ConfigurationProperties(prefix = "docoder")
@Component
public class DocoderConfig {

    @ApiModelProperty("数据目录")
    private String dataDir;

    @ApiModelProperty("token失效时间")
    private long tokenTimeSecond;

    @ApiModelProperty("记住我:token失效时间")
    private long tokenTimeRememberSecond;

    @ApiModelProperty("认证接口url")
    private String dispatcherAuthenticationUrl;

    @ApiModelProperty("token失效时间")
    private String tokenHeaderKey;

    @ApiModelProperty("web端缓存id-key")
    private String webCacheKeyId;

    @ApiModelProperty("web端缓存token-key")
    private String webCacheKeyToken;

    public void setDataDir(String dataDir) {
        if(StrUtil.isNotBlank(dataDir)){
            dataDir = dataDir.replace("\\", File.separator);
            dataDir = dataDir.replace("/", File.separator);
            this.dataDir = dataDir;
        }
    }

}
