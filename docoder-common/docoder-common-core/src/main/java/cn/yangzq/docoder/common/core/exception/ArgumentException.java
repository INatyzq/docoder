package cn.yangzq.docoder.common.core.exception;

/**
 * @author yangzq
 * @description 参数异常
 **/
public class ArgumentException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public ArgumentException(String msg) {
        super(msg);
    }

    public ArgumentException(int code, String msg) {
        super(code, msg);
    }

}
