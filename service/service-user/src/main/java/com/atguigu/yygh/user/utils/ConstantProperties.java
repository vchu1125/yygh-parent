package com.atguigu.yygh.user.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Weizhu
 * @Date 2023/8/15 18:40
 * @注释
 */
@Configuration
@ConfigurationProperties(prefix = "wx.open")//读取节点
@Data
public class ConstantProperties {
    private String appId;
    private String appSecret;
    private String redirectUri;
    private String sytBaseUrl;
}
