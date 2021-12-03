package cn.yangzq.docoder.common.core.exception;


import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
 * @author yangzq
 * @description 资源未找到异常
 **/
public class NotFoundException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public NotFoundException(String msg){
        super(msg);
    }

    public NotFoundException(int code, String msg) {
        super(code, msg);
    }

    public NotFoundException(String msg, Object data) {
        super(msg, data);
    }

    @Override
    public int getCode() {
        return StatusCode.NOT_FOUND;
    }

}
