package cn.yangzq.docoder.security.server.handler;

import cn.yangzq.docoder.common.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
*@author yangzq
*@description 用户上下文持有类抽象
*/
public abstract class AbstractUserContextHolder {

    protected final ThreadLocalPrincipal threadLocalPrincipal = new ThreadLocalPrincipal();

    protected HttpServletRequest request;
    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    private void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public AbstractUserContextHolder(){
        init();
    }

    /**
     * 初始化时会执行的方法
     */
    protected void init(){}

    /**
     * 获取当前用户
     * @return
     */
    protected abstract Object getCurrentUser();

}
