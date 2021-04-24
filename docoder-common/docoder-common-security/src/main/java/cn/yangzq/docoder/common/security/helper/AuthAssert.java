package cn.yangzq.docoder.common.security.helper;

import org.springframework.security.authentication.BadCredentialsException;

/**
*@author yangzq
*@description 认证断言类
*/
class AuthAssert {

    /**
     * 根据断言结果抛出错误消息
     * @param assertResult
     * @param errorMsg
     * @return
     */
    public AuthAssert authError(boolean assertResult,String errorMsg){
        if(assertResult){
            throw new BadCredentialsException(errorMsg);
        }
        return this;
    }

    /**
     * 根据断言结果抛异常
     * @param assertResult
     * @param exception
     * @return
     * @throws Exception
     */
    public AuthAssert authError(boolean assertResult,Exception exception) throws Exception {
        if(assertResult){
            throw exception;
        }
        return this;
    }

}
