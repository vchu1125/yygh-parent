package com.atguigu.yygh.oss.utils;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Weizhu
 * @Date 2023/8/17 11:28
 * @注释
 */
@Configuration
@ConfigurationProperties(prefix="aliyun.oss") //读取节点
@Data
public class ConstantProperties {

    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
}
