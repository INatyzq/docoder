package cn.yangzq.docoder.security.server.handler;

import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.core.exception.BasicException;
import cn.yangzq.docoder.common.core.handler.ExceptionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
*@author yangzq
*@description 未授权的统一处理方式
**/
@Slf4j
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private Exception exception;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException1) throws IOException, ServletException {
        //响应设置
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        int code = HttpStatus.UNAUTHORIZED.value();
        String msg = "认证失败：服务异常，请联系管理员！";
        boolean isBe = false;
        if(exception instanceof BasicException){
            isBe = true;
            BasicException be = (BasicException) exception;
            code = be.getCode();
            msg = exception.getMessage();
        }
        String path = request.getRequestURI();
        String exceptionInfo = JSONUtil.toJsonStr(ExceptionEntity.builder().code(code).error(exception.getClass().getSimpleName()).path(path).message(msg));
        response.getWriter().write(exceptionInfo);
        if(isBe){
            log.error("UnauthorizedEntryPoint("+path+"):{}",msg);
        }else{
            log.error("UnauthorizedEntryPoint("+path+"):",exception);
        }
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
