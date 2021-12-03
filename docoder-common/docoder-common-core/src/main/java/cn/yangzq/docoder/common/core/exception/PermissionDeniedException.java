package cn.yangzq.docoder.common.core.exception;


import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
 * @author yangzq
 * @description 权限拒绝异常
 **/
public class PermissionDeniedException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public PermissionDeniedException(String msg){
        super(msg);
    }

    public PermissionDeniedException(int code, String msg) {
        super(code, msg);
    }

    @Override
    public int getCode() {
        return StatusCode.PERMISSION_DENIED;
    }

}
