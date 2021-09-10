package cn.yangzq.docoder.security.server.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

/**
*@author yangzq
*@description 认证元数据缓存数据
**/
@ApiModel("认证缓存元数据")
@Data
public class AuthMetaData {

    @ApiModelProperty("缓存元数据key")
    public static final String AUTH_META_KEY = "AUTH:" + "META:";

    @ApiModelProperty("缓存主体key")
    public static final String AUTH_PRINCIPAL_KEY = "AUTH:" + "PRINCIPAL:";

    @ApiModelProperty("默认模块名称")
    public static final String DEFAULT_APP_MODULE = "DOCODER:";

    @ApiModelProperty("认证主体ID")
    private String id;

    @ApiModelProperty("终端名称和对应token的映射map")
    private Map<String, Set<String>> terminalTokenMap = new HashMap<>();

    @ApiModelProperty("终端名称列表")
    private Set<String> terminalList = new HashSet<>();



}
