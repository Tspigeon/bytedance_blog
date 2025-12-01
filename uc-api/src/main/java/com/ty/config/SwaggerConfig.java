package com.ty.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("Api")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ty.controller"))
                .build()
                .apiInfo(createApiInfo())
                .enable(true)//打开丝袜哥
                ;
    }
    @Bean
    public ApiInfo createApiInfo() {
        return new ApiInfo(
                "UnderCloud-Blog Api Swagger",
                "UnderCloud-Blog Api Documentation",
                "3.0",
                "#",
                new Contact("Mr.Biao", "#", "404837319@qq.com"),
                "#",
                "#",
                new ArrayList<>()
        );
    }



    @Bean
    public Docket createBackApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("Back")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ty.controller.back"))
                .build()
                .apiInfo(createBackInfo())
                .enable(true)//打开丝袜哥
                ;
    }
    @Bean
    public ApiInfo createBackInfo() {
        return new ApiInfo(
                "UnderCloud-Blog Back Swagger",
                "UnderCloud-Blog Back Documentation",
                "3.0",
                "#",
                new Contact("Mr.Biao", "#", "404837319@qq.com"),
                "#",
                "#",
                new ArrayList<>()
        );
    }
}
