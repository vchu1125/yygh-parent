package com.atguigu.yygh.cmn.client;

import com.atguigu.yygh.cmn.client.impl.DictFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Weizhu
 * @Date 2023/8/10 9:57
 * @注释
 */
@FeignClient(value = "service-cmn" ,contextId = "dictClient",fallback = DictFeignClientFallback.class)
public interface DictFeignClient {
    //获取数据字典名称
    @GetMapping(value = "/inner/cmn/dict/getName/{dictTypeId}/{value}")
    String getName(@PathVariable("dictTypeId") Long dictTypeId,
                          @PathVariable("value") String value);
}
