package cn.yangzq.docoder.common.security.filter;

import cn.yangzq.docoder.common.security.security.UnauthorizedEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public TokenAuthenticationFilter(AuthenticationManager authManager, UnauthorizedEntryPoint unauthorizedEntryPoint) {
        super(authManager);
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        Authentication authentication = null;
        try {
            //authentication = authenticationProvider.getAuthentication(req,res);
        } catch (Exception e) {
            unauthorizedEntryPoint.commence(req,res,new BadCredentialsException(e.getMessage()));
            return;
        }

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }
}