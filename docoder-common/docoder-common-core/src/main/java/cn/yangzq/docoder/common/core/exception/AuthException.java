package cn.yangzq.docoder.common.core.exception;


import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
 * @author yangzq
 * @description 认证异常
 **/
public class AuthException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(int code, String msg) {
        super(code, msg);
    }

    public AuthException(String msg,boolean isHandle) {
        super(msg,isHandle);
    }

    @Override
    public int getCode() {
        return StatusCode.AUTH_FAILED;
    }

}
