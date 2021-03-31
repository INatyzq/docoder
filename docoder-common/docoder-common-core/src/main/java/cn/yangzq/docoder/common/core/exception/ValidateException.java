package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 认证异常
**/
public class ValidateException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public ValidateException(String msg){
        super(402,msg);
    }

    public ValidateException(int code, String msg){
        super(code,msg);
    }

    public ValidateException(String msg, boolean isHandle){
        super(msg,isHandle);
    }

}
