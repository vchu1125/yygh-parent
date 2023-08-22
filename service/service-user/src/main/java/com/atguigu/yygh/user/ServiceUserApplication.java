package com.atguigu.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Weizhu
 * @Date 2023/8/15 18:34
 * @注释
 */
@SpringBootApplication
@ComponentScan("com.atguigu.yygh")
@EnableFeignClients(basePackages = "com.atguigu.yygh")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class,args);
    }
}
