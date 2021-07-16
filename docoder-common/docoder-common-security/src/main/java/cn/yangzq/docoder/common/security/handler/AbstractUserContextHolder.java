package cn.yangzq.docoder.common.security.handler;

import cn.yangzq.docoder.common.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
*@author yangzq
*@description 用户上下文持有类抽象
*/
public abstract class AbstractUserContextHolder {

    /*protected final ThreadLocal<String> userDetailJsonStrLocal = new ThreadLocal<>();*/

    protected HttpServletRequest request;
    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取当前用户
     * @return
     */
    protected abstract Object getCurrentUser();

    /**
     * 通过token获取当前用户
     * @return
     */
    protected abstract Object getCurrentUser(String token);

}
