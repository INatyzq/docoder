package cn.yangzq.docoder.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
*@author yangzq
*@description mybatis plus统一配置
**/
@Configuration
public class MybatisConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SqlFilterArgumentResolver());
    }

    /**
     * 配置mybatis plus分页插件,
     * 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.SQL_SERVER2005);
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(true);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(100L);
        // 开启 count 的 join 优化,只针对部分 left join
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setUseDeprecatedExecutor(false);
            configuration.setObjectWrapperFactory(new MapWrapperFactory());
        };
    }

    /**
     * 元数据处理
     */
    /*@Bean
    @ConditionalOnBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler(@Qualifier("MybatisMetaObjectHandler") MetaObjectHandler handler){
        return handler;
    }*/
}
