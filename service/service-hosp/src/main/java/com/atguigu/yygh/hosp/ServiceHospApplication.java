package com.atguigu.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Weizhu
 * @Date 2023/7/24 17:36
 * @注释
 */
@SpringBootApplication
@ComponentScan("com.atguigu.yygh")
@EnableFeignClients(basePackages = "com.atguigu.yygh")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}
