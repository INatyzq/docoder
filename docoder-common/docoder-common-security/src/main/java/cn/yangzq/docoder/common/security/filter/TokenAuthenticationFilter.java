package cn.yangzq.docoder.common.security.filter;

import cn.yangzq.docoder.common.security.handler.AuthenticationProvider;
import cn.yangzq.docoder.common.security.security.UnauthorizedEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
*@author yangzq
*@description  访问过滤器
**/
@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    private AuthenticationProvider authenticationProvider;

    public TokenAuthenticationFilter(AuthenticationManager authManager,AuthenticationProvider authenticationProvider,UnauthorizedEntryPoint unauthorizedEntryPoint) {
        super(authManager);
        this.authenticationProvider = authenticationProvider;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        boolean isDispatcher = false;
        try {
            isDispatcher = authenticationProvider.authentication(req,res);
        } catch (Exception e) {
            unauthorizedEntryPoint.commence(req,res,new BadCredentialsException(e.getMessage()));
            return;
        }

        if(!isDispatcher){
            chain.doFilter(req, res);
        }
    }
}