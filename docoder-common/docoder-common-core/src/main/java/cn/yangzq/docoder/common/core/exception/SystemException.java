package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 系统异常
**/
public class SystemException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public SystemException(String msg){
        super(msg);
    }

    public SystemException(int code, String msg){
        super(code,msg);
    }

    public SystemException(Throwable throwable){
        super(throwable);
    }

}
