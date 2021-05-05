package cn.yangzq.docoder.common.security.handler;

/**
*@author yangzq
*@description 用户上下文持有类抽象
*/
public abstract class AbstractUserContextHolder {

    protected final ThreadLocal<String> userDetailJsonStrLocal = new ThreadLocal<>();

    /**
     * 获取当前用户
     * @param Object
     * @return
     */
    protected abstract Object getCurrentUser();

}
