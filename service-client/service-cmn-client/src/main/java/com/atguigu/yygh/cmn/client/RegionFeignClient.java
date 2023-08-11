package com.atguigu.yygh.cmn.client;

import com.atguigu.yygh.cmn.client.impl.RegionFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Weizhu
 * @Date 2023/8/10 10:06
 * @注释
 */

@FeignClient(value = "service-cmn",contextId = "regionClient",fallback = RegionFeignClientFallback.class)
public interface RegionFeignClient {

    //根据地区编码获取地区名称
    @GetMapping(value = "/inner/cmn/region/getName/{code}")
    String getName(@PathVariable("code") String code);
}
