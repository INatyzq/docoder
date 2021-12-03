package cn.yangzq.docoder.common.core.exception;


import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
 * @author yangzq
 * @description 密码不正确异常
 **/
public class PasswordIncorrectException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public PasswordIncorrectException(String msg){
        super(msg);
    }

    public PasswordIncorrectException(int code, String msg) {
        super(code, msg);
    }

    public PasswordIncorrectException(String msg,boolean isHandle) {
        super(msg,isHandle);
    }

    @Override
    public int getCode() {
        return StatusCode.PASSWORD_INCORRECT;
    }

}
