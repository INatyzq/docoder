package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 认证异常
**/
public class AuthException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public AuthException(String msg){
        super(401,msg);
    }

    public AuthException(int code, String msg){
        super(code,msg);
    }

}
