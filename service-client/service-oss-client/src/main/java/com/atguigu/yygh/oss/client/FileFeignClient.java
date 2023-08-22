package com.atguigu.yygh.oss.client;

import com.atguigu.yygh.oss.client.impl.FileFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Weizhu
 * @Date 2023/8/18 18:43
 * @注释
 */
@FeignClient(
        value = "service-oss",
        contextId = "FileFeignClient",
        fallback = FileFeignClientFallback.class
)
public interface FileFeignClient {
    @GetMapping("/inner/oss/file/getPreviewUrl")
    String getPreviewUrl(@RequestParam("objectName") String objectName);
}
