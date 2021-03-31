package cn.yangzq.docoder.common.swagger.config;

import cn.yangzq.docoder.common.swagger.constant.SwaggerVersion;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
*@author yangzq
*@description SwaggerConfiguration
**/
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {

    /**
     * 默认文档模块
     */
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.uyeek.edu"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 基础模块API
     */
    @Bean
    public Docket baseRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(SwaggerVersion.MODULE_BASE.toString())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.uyeek.edu.service.base"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 课程模块API
     */
    @Bean
    public Docket courseRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(SwaggerVersion.MODULE_COURSE.toString())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.uyeek.edu.service.course"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 班牌模块API
     */
    @Bean
    public Docket attendanceRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(SwaggerVersion.MODULE_CLASS_CARD.toString())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.uyeek.edu.service.classcard"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 文档基础信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("EDU·优一科")
                .description("测试接口API列表")
                .termsOfServiceUrl("http://localhost:8090/")
                .contact(new Contact("yangzq","",""))
                .version("1.0")
                .build();
    }
}
