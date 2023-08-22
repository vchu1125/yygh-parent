package com.atguigu.yygh.oss.client.impl;

import com.atguigu.yygh.oss.client.FileFeignClient;
import org.springframework.stereotype.Component;

/**
 * @Author Weizhu
 * @Date 2023/8/18 18:45
 * @注释
 */
@Component
public class FileFeignClientFallback implements FileFeignClient {
    @Override
    public String getPreviewUrl(String objectName) {
        return "图片显示失败";
    }
}
