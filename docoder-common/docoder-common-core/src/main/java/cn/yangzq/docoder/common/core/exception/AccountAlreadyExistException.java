package cn.yangzq.docoder.common.core.exception;


import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
 * @author yangzq
 * @description 账户没有找到异常
 **/
public class AccountAlreadyExistException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public AccountAlreadyExistException(String msg) {
        super(msg);
    }

    public AccountAlreadyExistException(int code, String msg) {
        super(code, msg);
    }

    public AccountAlreadyExistException(String msg, boolean isHandle) {
        super(msg,isHandle);
    }

    @Override
    public int getCode() {
        return StatusCode.ACCOUNT_ALREADY_EXIST;
    }
}
