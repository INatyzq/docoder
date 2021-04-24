package cn.yangzq.docoder.common.security.helper;

import java.util.ArrayList;
import java.util.List;

/**
*@author yangzq
*@description 公共认证接口
*/
abstract class AbstractAuthProvider {

    protected List<Authenticator> authenticators = new ArrayList<>();

    /**
     * 认证方法
     * @param authAssert
     */
    abstract Authenticator authenticate();

}
