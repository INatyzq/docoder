package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 未知的文件类型异常
**/
public class UnknownFileTypeException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public UnknownFileTypeException(String msg){
        super(msg);
    }

    public UnknownFileTypeException(int code, String msg){
        super(code,msg);
    }

    public UnknownFileTypeException(Throwable throwable){
        super(throwable);
    }

}
