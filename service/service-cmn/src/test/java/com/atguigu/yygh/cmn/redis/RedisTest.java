package com.atguigu.yygh.cmn.redis;

import com.atguigu.yygh.cmn.service.RegionService;
import com.atguigu.yygh.model.cmn.Region;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/2 20:01
 * @注释
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RegionService regionService;


    @Test
    public void testRedisSave(){
        redisTemplate.opsForValue().set("username1","xianggua");
        redisTemplate.opsForValue().set("region",new Region());

        redisTemplate.opsForList().leftPush("list","zhangfei");
        redisTemplate.opsForList().leftPush("list","guanyu");

        redisTemplate.opsForSet().add("set","aa","bb","cc");

        redisTemplate.opsForZSet().add("zSet","aaa",1);
        redisTemplate.opsForZSet().add("zSet","bbb",2);
        redisTemplate.opsForZSet().add("zSet","ccc",3);

        Map<String,String> maps = new HashMap();
        maps.put("1","星期一");
        maps.put("2","星期二");
        redisTemplate.opsForHash().putAll("week",maps);

    }

    @Test
    public void testSaveRegionWithCacheManager(){
        Region region = new Region();
        region.setName("test");
        regionService.saveRegionWithCacheManager(region);
    }

    @Test
    public void testDeleteRegionWithCacheManager() {
        regionService.deleteRegionWithCacheManager(3713L);
    }
}
