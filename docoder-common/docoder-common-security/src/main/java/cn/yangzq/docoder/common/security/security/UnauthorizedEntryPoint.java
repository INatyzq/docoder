package cn.yangzq.docoder.common.security.security;

import cn.hutool.json.JSONUtil;
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

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        //响应设置
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        int code = HttpStatus.UNAUTHORIZED.value();
        String exceptionInfo = JSONUtil.toJsonStr(ExceptionEntity.builder().code(code).error(authException.getClass().getSimpleName()).path(request.getRequestURI()).message(authException.getMessage()));
        response.getWriter().write(exceptionInfo);
        log.error(exceptionInfo);
    }
}
