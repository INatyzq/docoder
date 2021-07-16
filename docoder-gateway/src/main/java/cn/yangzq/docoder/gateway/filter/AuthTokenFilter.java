package cn.yangzq.docoder.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.user.api.IUserService;
import cn.yangzq.docoder.user.entity.UserDetail;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
*@author yangzq
*@description token认证filter
**/
@Component
public class AuthTokenFilter implements GlobalFilter, Ordered {

    @DubboReference
    private IUserService userService;

    private static final List<String> IGNORE_URL_LIST = new ArrayList<>();

    static {
        IGNORE_URL_LIST.add("/user/registerPrepare");
        IGNORE_URL_LIST.add("/user/refreshCaptcha");
        IGNORE_URL_LIST.add("/user/login");
        IGNORE_URL_LIST.add("/user/refresh");
        IGNORE_URL_LIST.add("/user/isRepeat");
        IGNORE_URL_LIST.add("/user/register");
    }

    /**
     * 当前组件order值
     */
    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 从请求头中获取token认证
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(isAllow(request.getURI().getPath())){
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst("web_token");
        JSONObject resultApi = JSONUtil.createObj();

        //token不存在时响应
        if(StrUtil.isBlank(token)){
            resultApi.append(String.valueOf(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value()),"请求被拒绝，缺失token！");
            return failedResponse(exchange,resultApi);
        }

        UserDetail userDetail = userService.getUserDetail(token);
        if(userDetail==null){
            resultApi.append(String.valueOf(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value()),"请求被拒绝，请携带有效凭证！");
            return failedResponse(exchange,resultApi);
        }

        return chain.filter(exchange);
    }

    /**
     * 认证失败响应体
     * @param exchange
     * @param jsonObject
     * @return
     */
    private Mono<Void> failedResponse(ServerWebExchange exchange, JSONObject jsonObject){
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(jsonObject).getBytes());
        return response.writeWith(Flux.just(buffer));
    }

    private boolean isAllow(String path){
        for(String ignore:IGNORE_URL_LIST){
            if(path.contains(ignore)){
                return true;
            }
        }
        return false;
    }
}
