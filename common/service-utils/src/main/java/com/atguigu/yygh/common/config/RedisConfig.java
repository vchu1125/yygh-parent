package com.atguigu.yygh.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Author Weizhu
 * @Date 2023/8/2 19:50
 * @注释
 */

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {

        //设置序列化器，否则redis自动使用对象的jdk序列化
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        //定义序列化器
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //设置序列化方案
        redisTemplate.setKeySerializer(stringRedisSerializer);//key序列化方式
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);//value序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);//hash key序列化方式
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);//hash value序列化

        //设置连接池
        //Lettuce基于Netty的连接实例（StatefulRedisConnection），
        //可以在多个线程间并发访问，即多个线程公用一个连接实例，且线程安全
        //同时它是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。
        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {

        //定义序列化器
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();


        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //过期时间600秒
                .entryTtl(Duration.ofSeconds(600))
                // 配置序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }
}