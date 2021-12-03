package cn.yangzq.docoder.common.core.exception;

import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
*@author yangzq
*@description 检验异常
**/
public class ValidateException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public ValidateException(String msg){
        super(msg);
    }

    public ValidateException(int code, String msg){
        super(code,msg);
    }

    public ValidateException(String msg, boolean isHandle){
        super(msg,isHandle);
    }

    @Override
    public int getCode() {
        return StatusCode.INTERNAL_SERVER_ERROR;
    }

}
