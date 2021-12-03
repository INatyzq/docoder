package cn.yangzq.docoder.common.core.exception;

import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
 * @author yangzq
 * @description 基础异常类
 **/
public class BasicException extends RuntimeException {

    protected static final long serialVersionUID = 543117603797797479L;

    protected int code = StatusCode.INTERNAL_SERVER_ERROR;

    protected Object data;

    protected boolean isHandle;

    public BasicException(String msg) {
        super(msg);
    }

    public BasicException(String msg, boolean isHandle) {
        super(msg);
        this.isHandle = isHandle;
    }

    public BasicException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BasicException(String msg, Object data) {
        super(msg);
        this.data = data;
    }

    public BasicException(Throwable throwable) {
        super(throwable);
    }

    public void code(int code) {
        this.code = code;
    }

    public void data(Object data) {
        this.data = data;
    }

    public void isHandle(boolean isHandle) {
        this.isHandle = isHandle;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setHandle(boolean handle) {
        isHandle = handle;
    }

    public boolean isHandle() {
        return isHandle;
    }
}
