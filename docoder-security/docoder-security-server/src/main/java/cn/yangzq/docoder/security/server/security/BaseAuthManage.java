package cn.yangzq.docoder.security.server.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.security.server.config.DocoderAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzq
 * @description 基础认证管理器
 **/
@Component
public class BaseAuthManage extends AuthManage {

    private static final String DEFAULT_TERMINAL = "default";
    private static final String PRINCIPAL_ID = "PRINCIPAL_ID";
    private static final String PRINCIPAL_MODULE = "PRINCIPAL_MODULE";
    private static final String PRINCIPAL_TERMINAL = "PRINCIPAL_TERMINAL";
    private static final String PRINCIPAL_JSON = "PRINCIPAL_JSON";


    @Autowired
    private DocoderAuthConfig authConfig;
    @Autowired
    private RedisUtil redisUtil;
    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String login(String id, Object principal) {
        return login(id, AuthMetaData.DEFAULT_APP_MODULE, DEFAULT_TERMINAL, principal);
    }

    @Override
    public String loginWithModule(String id, String moduleName, Object principal) {
        return login(id, moduleName, DEFAULT_TERMINAL, principal);
    }

    @Override
    public String loginWithTerminal(String id, String terminalName, Object principal) {
        return login(id, AuthMetaData.DEFAULT_APP_MODULE, terminalName, principal);
    }

    @Override
    public String login(String id, String moduleName, String terminalName, Object principal) {
        boolean allowMultiplePrincipal = authConfig.isAllowMultiplePrincipal();
        boolean allowMultipleTerminal = authConfig.isAllowMultipleTerminal();
        long expireTimeMillisecond = authConfig.getExpireTimeMillisecond();

        //默认值
        moduleName = StrUtil.blankToDefault(moduleName, AuthMetaData.DEFAULT_APP_MODULE);
        if (StrUtil.isBlank(terminalName) || !allowMultipleTerminal) {
            terminalName = DEFAULT_TERMINAL;
        }

        String authMetaKey = AuthMetaData.AUTH_META_KEY + moduleName + ":" + terminalName + ":" + id;
        String authMetaVal = redisUtil.get(authMetaKey);
        AuthMetaData metaData = StrUtil.isBlank(authMetaVal) ? new AuthMetaData() : JSONUtil.toBean(authMetaVal, AuthMetaData.class);

        //终端名设置
        Set<String> terminalList = metaData.getTerminalList();
        terminalList.add(terminalName);

        //生成token
        String token = JWTUtil.sign(id, expireTimeMillisecond);
        String tokenKey = AuthMetaData.AUTH_PRINCIPAL_KEY + token;

        Map<String, Set<String>> terminalTokenMap = metaData.getTerminalTokenMap();

        Set<String> terminals = terminalTokenMap.computeIfAbsent(terminalName, list -> new HashSet<>());
        if (!allowMultiplePrincipal) {
            terminals.forEach(tokenVal -> redisUtil.delete(tokenVal));
            terminals.clear();
        }
        terminals.add(tokenKey);

        //放入缓存
        redisUtil.setEx(authMetaKey, JSONUtil.toJsonStr(metaData), expireTimeMillisecond, TimeUnit.SECONDS);
        redisUtil.hPut(tokenKey, PRINCIPAL_ID, id);
        redisUtil.hPut(tokenKey, PRINCIPAL_JSON, JSONUtil.toJsonStr(principal));
        redisUtil.hPut(tokenKey, PRINCIPAL_MODULE, moduleName);
        redisUtil.hPut(tokenKey, PRINCIPAL_TERMINAL, terminalName);
        redisUtil.expire(tokenKey, expireTimeMillisecond, TimeUnit.SECONDS);

        return null;
    }

    @Override
    public void logout() {
        String authHeaderKey = authConfig.getAuthHeaderKey();
        long expireTimeMillisecond = authConfig.getExpireTimeMillisecond();
        String token = request.getHeader(authHeaderKey);

        if (StrUtil.isBlank(token)) {
            return;
        }
        String tokenKey = AuthMetaData.AUTH_PRINCIPAL_KEY + token;
        String principalId = redisUtil.hGet(tokenKey, PRINCIPAL_ID).toString();
        String principalModule = redisUtil.hGet(tokenKey, PRINCIPAL_MODULE).toString();
        String principalTerminal = redisUtil.hGet(tokenKey, PRINCIPAL_TERMINAL).toString();

        //删除token
        redisUtil.delete(tokenKey);
        //删除认证元数据
        boolean allowMultiplePrincipal = authConfig.isAllowMultiplePrincipal();
        boolean allowMultipleTerminal = authConfig.isAllowMultipleTerminal();

        String authMetaKey = AuthMetaData.AUTH_META_KEY + principalModule + principalId;
        if (!allowMultiplePrincipal && !allowMultipleTerminal) {
            redisUtil.delete(authMetaKey);
            return;
        }

        String authMetaVal = redisUtil.get(authMetaKey);
        AuthMetaData metaData = JSONUtil.toBean(authMetaVal, AuthMetaData.class);

        Set<String> tokens = metaData.getTerminalTokenMap().get(principalTerminal);
        tokens.remove(tokenKey);

        if (tokens.size() == 0) {
            metaData.getTerminalList().remove(principalTerminal);
        }

        redisUtil.setEx(authMetaKey, JSONUtil.toJsonStr(metaData), expireTimeMillisecond, TimeUnit.SECONDS);

    }
}
