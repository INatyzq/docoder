package cn.yangzq.docoder.common.core.exception;


import cn.yangzq.docoder.common.core.enums.StatusCode;

/**
*@author yangzq
*@description 未知的文件类型异常
**/
public class UnknownFileTypeException extends BasicException{

    private static final long serialVersionUID = 4007531827543988324L;

    public UnknownFileTypeException(String msg){
        super(msg);
    }

    @Override
    public int getCode() {
        return StatusCode.UNKNOWN_FILE_TYPE;
    }

}
