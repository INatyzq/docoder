package cn.yangzq.docoder.common.core.exception;

/**
 * @author yangzq
 * @description 数据重复异常
 **/
public class DataRepeatException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public DataRepeatException(String msg) {
        super(msg);
    }

    public DataRepeatException(int code, String msg) {
        super(code, msg);
    }

    public DataRepeatException(String msg, Object data) {
        super(msg, data);
    }

}
