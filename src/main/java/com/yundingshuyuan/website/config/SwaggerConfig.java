package com.yundingshuyuan.website.config;

import com.yundingshuyuan.website.constants.SysConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fant.J.
 * 2018/4/30 17:20
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.yundingshuyuan.website.controller";
    public static final String VERSION = "3.0.0";

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("YUNDING WEBSITE SYS API")
                .description("云顶书院--云顶官网")
                .version(VERSION)
                .contact(new Contact("Leeyf", "", "1042245208@qq.com"))
                .build();
    }

    @Bean
    public Docket customImplementation() {
        //return new Docket(DocumentationType.SWAGGER_2)
        //        .select()
        //        .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
        //        .build()
        //        .host("localhost:8080")
        //        .apiInfo(apiInfo());
        //添加head参数start
        ParameterBuilder tokenParam = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        tokenParam.name(SysConstant.HEADER_TOKEN)
                .description("accessToken")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        parameters.add(tokenParam.build());
        //添加head参数end

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
//                .host("yundingshuyuan.com:9200")
                .host("localhost:8081")
                ;
    }

}
