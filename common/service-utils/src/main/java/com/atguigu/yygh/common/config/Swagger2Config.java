package com.atguigu.yygh.common.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Author Weizhu
 * @Date 2023/7/24 18:45
 * @注释
 */
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {
    @Bean
    public Docket docket(){
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("尚医通管理系统 APIs")
                        .contact("admin@atguigu.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("管理平台")
                .select()
                .paths(PathSelectors.regex("/admin/.*"))
                .build();
        return docket;
    }

    @Bean
    public Docket docket2(){
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("尚医通管理系统 APIs")
                        .contact("admin@atguigu.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("医院API")
                .select()
                .paths(PathSelectors.regex("/api/.*"))
                .build();
        return docket;
    }

    @Bean
    public Docket docketInner() {
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("尚医通管理系统 APIs")
                        .contact("admin@atguigu.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("微服务内部调用")
                .select()
                .paths(PathSelectors.regex("/inner/.*"))
                .build();
        return docket;
    }

    @Bean
    public Docket frontDocket(){
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("尚医通管理系统 APIs")
                        .contact("admin@atguigu.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("用户平台")
                .select()
                .paths(PathSelectors.regex("/front/.*"))
                .build();
        return docket;
    }


}
