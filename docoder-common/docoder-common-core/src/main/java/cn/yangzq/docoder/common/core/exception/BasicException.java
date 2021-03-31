package cn.yangzq.docoder.common.core.exception;

/**
*@author yangzq
*@description 基础异常类
**/
public class BasicException extends RuntimeException{

    private static final long serialVersionUID = 543117603797797479L;

    private int code = 500;

    private boolean isHandle;

    public BasicException(String msg){
        super(msg);
    }

    public BasicException(String msg,boolean isHandle){
        super(msg);
        this.isHandle = isHandle;
    }

    public BasicException(int code,String msg){
        super(msg);
        this.code = code;
    }

    public BasicException(Throwable throwable){
        super(throwable);
    }

    public int getCode() {
        return code;
    }

    public void setHandle(boolean handle) {
        isHandle = handle;
    }

    public boolean isHandle() {
        return isHandle;
    }
}
