package com.baizhi.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ls
 * @date 2021/6/4 - 8:25
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).pathMapping("/").select()
                .apis(RequestHandlerSelectors.basePackage("com.baizhi.contrloller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder().title("标题")
                .description("标题的详细描述")
                .version("1.1")
                .contact(new Contact("liushuan","www.baizhi.com","22928732@.com"))
                .license("The baizhi License")
                .licenseUrl("http://www.baizhi.com")
                .build());
    }

}
