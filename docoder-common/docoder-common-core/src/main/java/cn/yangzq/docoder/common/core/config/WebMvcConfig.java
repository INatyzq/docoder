package cn.yangzq.docoder.common.core.config;

import cn.hutool.core.date.DatePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 *@author yangzq
 *@description 注入自自定义SQL、过滤
 **/
@Configuration
@ConditionalOnWebApplication(type = SERVLET)
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestMappingHandlerAdapter mappingHandlerAdapter;

    /**
     * GET请求参数中时间类型转换
     * <ul>
     * <li>HH:mm:ss yyyy-MM-dd-> LocalTime</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));

        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.UTC_SIMPLE_PATTERN));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.UTC_PATTERN));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.UTC_MS_PATTERN));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.UTC_MS_WITH_ZONE_OFFSET_PATTERN));
        registrar.registerFormatters(registry);
    }

    /**
     * 日期格式化配置
     */
    @PostConstruct
    public void initEditableValidation(){
        ConfigurableWebBindingInitializer webBindingInitializer = (ConfigurableWebBindingInitializer) mappingHandlerAdapter.getWebBindingInitializer();
        if(webBindingInitializer!=null){
            GenericConversionService conversionService = (GenericConversionService) webBindingInitializer.getConversionService();
            conversionService.addConverter(new CustomDateConverter());
        }
    }

    /**
     * 注册跨域访问过滤器
     */
    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(corsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(1);

        return registration;
    }

    /**
     * 跨域访问过滤器配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration conf = new CorsConfiguration();
        conf.addAllowedHeader("*");
        conf.addAllowedMethod("*");
        conf.addAllowedOrigin("*");
        conf.setAllowCredentials(true);
        conf.setMaxAge(3600L);
        conf.addExposedHeader(HttpHeaders.SET_COOKIE);
        conf.addExposedHeader(HttpHeaders.SET_COOKIE2);
        conf.addExposedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
        conf.addExposedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
        conf.addExposedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
        conf.addExposedHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE);
        conf.addExposedHeader("X-Frame-Options");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
