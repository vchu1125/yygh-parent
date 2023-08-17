package com.atguigu.yygh.common.utils;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Weizhu
 * @Date 2023/8/17 16:04
 * @注释 ：授权校验
 */
@Component
public class AuthContextHolder {
    @Resource
    private RedisTemplate redisTemplate;

    public Long checkAuth(HttpServletRequest request) {
        //从http请求头中获取token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new YyghException(ResultCodeEnum.LOGIN_AUTH);
        }
        Object userIdObj = redisTemplate.opsForValue().get("user:token:" + token);

        //数据存入redis时，按照实际的大小分配空间，取出时int能存下，默认使用int类型，int存不下再用long
        //我们无法判断redis中存储的是什么类型的id，因此在此做一个转换
        Long userId = null;
        if (userIdObj instanceof Integer) {
            userId = ((Integer) userIdObj).longValue();
        } else if (userIdObj instanceof Long) {
            userId = (Long) userIdObj;
        } else if (userIdObj instanceof String) {
            userId = Long.parseLong(userIdObj.toString());
        }

        if (StringUtils.isEmpty(userId)) {
            throw new YyghException(ResultCodeEnum.LOGIN_AUTH);
        }
        return userId;
    }
}
