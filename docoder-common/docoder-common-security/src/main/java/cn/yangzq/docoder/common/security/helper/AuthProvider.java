package cn.yangzq.docoder.common.security.helper;

import org.springframework.stereotype.Component;

/**
*@author yangzq
*@description web端认证器
*/
@Component
public class AuthProvider extends AbstractAuthProvider{

    @Override
    public Authenticator authenticate( ) {
        Authenticator authenticator = new Authenticator();
        this.authenticators.add(authenticator);
        return authenticator;
    }
}
