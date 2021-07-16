package cn.yangzq.docoder.user.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.security.handler.AbstractUserContextHolder;
import cn.yangzq.docoder.user.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yangzq
 * @description 用户上下文持有类
 */
@Component
public class UserContextHolder extends AbstractUserContextHolder {

    @Autowired
    private DocoderConfig config;

    @Override
    public UserDetail getCurrentUser() {
        String token = null;
        try {
            token = request.getHeader(config.getTokenHeaderKey());
        } catch (Exception e) {
            return null;
        }
        return getUserByRedis(token);
    }

    @Override
    public UserDetail getCurrentUser(String token) {
        return getUserByRedis(token);
    }

    private UserDetail getUserByRedis(String token) {
        String certificate = null;
        try {
            String idKey = config.getWebCacheKeyToken() + token;
            String userKey = redisUtil.get(idKey).split("_")[0];
            certificate = redisUtil.get(config.getWebCacheKeyId()+userKey);
            if (StrUtil.isBlank(certificate)) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return JSONUtil.toBean(certificate, UserDetail.class);
    }
}
