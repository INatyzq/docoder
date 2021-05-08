package cn.yangzq.docoder.user.config;

import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.user.entity.UserDetail;
import cn.yangzq.docoder.common.security.handler.AbstractUserContextHolder;
import org.springframework.stereotype.Component;
/**
*@author yangzq
*@description 用户上下文持有类
*/
@Component
public class UserContextHolder extends AbstractUserContextHolder {

    @Override
    public UserDetail getCurrentUser() {
        return JSONUtil.toBean(userDetailJsonStrLocal.get(),UserDetail.class);
    }
}
