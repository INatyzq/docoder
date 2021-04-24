package cn.yangzq.docoder.common.security.config;

import cn.yangzq.docoder.common.security.filter.TokenAuthenticationFilter;
import cn.yangzq.docoder.common.security.security.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
*@author yangzq
*@description  Security配置类
**/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    public WebSecurityConfig(UnauthorizedEntryPoint unauthorizedEntryPoint) {
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }

    /**
     * 配置设置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.

            cors().and()
            //跨域访问资源配置
            //.configurationSource(corsConfigurationSource()).and()
            // 由于使用的是JWT，我们这里不需要csrf
            .csrf().disable()
            //异常处理器
            .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
            .and().authorizeRequests()
            //对preflight放行
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()
            // 除上面外的所有请求全部需要鉴权认证
            .addFilter(new TokenAuthenticationFilter(authenticationManager(), unauthorizedEntryPoint)).httpBasic();
    }

    /**
     * 配置哪些请求不拦截
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**","/**/doc.html",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/base/sys/sysUser/**","/app/login/deviceDetails/**"
               );
    }

    /**
     * 跨域访问配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.addAllowedHeader("*");
        //configuration.setAllowedHeaders(Arrays.asList("Sec-Fetch-Site", "Sec-Fetch-Mode", "Sec-Fetch-Dest", "Upgrade", "Sec-WebSocket-Version", "Sec-WebSocket-Key", "Sec-WebSocket-Extensions", "Pragma", "User-Agent", "If-None-Match", "Connection", "Cache-Control", "Accept-Language", "Accept-Encoding", "X-Requested-With", "Host", "Referer", "Origin", "Cookie", "Cookie2", "Content-Type", "Content-Length", "Accept", "Content-Type", "x-device-user-agent", "token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}