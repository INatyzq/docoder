package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 未知的文件类型异常
**/
public class NoSupportFileTypeException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public NoSupportFileTypeException(String msg){
        super(msg);
    }

    public NoSupportFileTypeException(int code, String msg){
        super(code,msg);
    }

    public NoSupportFileTypeException(Throwable throwable){
        super(throwable);
    }

}
