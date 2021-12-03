package cn.yangzq.docoder.common.security;

import cn.dev33.satoken.config.SaTokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangzq
 * @description 组件配置扫描并自动配置
 **/
@ComponentScan("cn.yangzq.docoder.common.security")
@Configuration
public class SecurityComponentConfig {

    /**
     * satoken默认配置
     *
     * @param config 配置类
     */
    //@Autowired
    public void configSaToken(SaTokenConfig config) {
        // token名称 (同时也是cookie名称)
        config.setTokenName("dctoken");
        //自定义token前缀
        config.setTokenPrefix("Bearer ");
        // token有效期，单位s 默认30天
        config.setTimeout(7 * 24 * 60 * 60);
        // token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒
        config.setActivityTimeout(-1);
        // 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
        config.setIsConcurrent(true);
        // 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
        config.setIsShare(true);
        // token风格
        config.setTokenStyle("tik");
        // 是否输出操作日志
        config.setIsLog(false);
    }

}
