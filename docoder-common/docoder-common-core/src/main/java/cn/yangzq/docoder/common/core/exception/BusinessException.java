package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 业务异常
**/
public class BusinessException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public BusinessException(String msg){
        super(msg);
    }

    public BusinessException(int code,String msg){
        super(code,msg);
    }

    public BusinessException(String msg,boolean isHandle){
        super(msg,true);
    }

    public BusinessException(Throwable throwable){
        super(throwable);
    }

}
