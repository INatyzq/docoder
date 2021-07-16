package cn.yangzq.docoder.common.core.config;

import cn.yangzq.docoder.common.core.exception.BasicException;

/**
*@author yangzq
*@description 认证异常
**/
public class ArgumentException extends BasicException {

    private static final long serialVersionUID = 4007531827543988324L;

    public ArgumentException(String msg){
        super(401,msg);
    }

    public ArgumentException(int code, String msg){
        super(code,msg);
    }

}
