package cn.yangzq.docoder.common.security.helper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
*@author yangzq
*@description 认证器
*/
public class Authenticator extends AuthAssert{

    private String cacheKey;

    private String cacheVal;

    private List<String> authHeaders;

    public Authenticator setCache(String cacheKey,Object cacheVal){
        this.cacheKey = cacheKey;
        this.cacheVal = JSONUtil.toJsonStr(cacheVal);
        return this;
    }

    public Authenticator setAuthHeader(String...authHeaders){
        this.authHeaders = authHeaders.length==0 ? new ArrayList<>(): Arrays.asList(authHeaders);
        return this;
    }

}
