package com.atguigu.yygh.cmn.client.impl;

import com.atguigu.yygh.cmn.client.RegionFeignClient;
import org.springframework.stereotype.Component;

/**
 * @Author Weizhu
 * @Date 2023/8/10 14:53
 * @注释
 */
@Component
public class RegionFeignClientFallback implements RegionFeignClient {
    @Override
    public String getName(String code) {
        return "暂时无法获取数据";
    }
}
