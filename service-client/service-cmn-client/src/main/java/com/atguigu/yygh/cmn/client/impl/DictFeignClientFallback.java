package com.atguigu.yygh.cmn.client.impl;

import com.atguigu.yygh.cmn.client.DictFeignClient;
import org.springframework.stereotype.Component;

/**
 * @Author Weizhu
 * @Date 2023/8/10 14:52
 * @注释
 */
@Component
public class DictFeignClientFallback implements DictFeignClient {
    @Override
    public String getName(Long dictTypeId, String value) {
        return "暂时无法获取数据";
    }
}
