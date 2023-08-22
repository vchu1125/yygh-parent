package com.atguigu.yygh.common.utils;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;

import com.atguigu.yygh.vo.user.UserVo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author Weizhu
 * @Date 2023/8/17 16:04
 * @注释 ：授权校验
 */
@Component
public class AuthContextHolder {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 检查授权状态并续期
     * @param request
     * @return
     */
    public Long checkAuth(HttpServletRequest request,HttpServletResponse response) {
        //从http请求头中获取token
        String token =  CookieUtils.getCookie(request,"token");
        if (StringUtils.isEmpty(token)) {
            //throw new YyghException(ResultCodeEnum.LOGIN_AUTH);
            return refreshToken(request,response);//刷新token
        }
        Object userIdObj = redisTemplate.opsForValue().get("user:token:" + token);
        if(userIdObj == null){
            return refreshToken(request,response);//刷新token
        }

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



    /**
     * 将token和refreshToken保存在redis和cookie中的通用方法
     * @param userVo
     * @param response
     */
    public void saveToken(UserVo userVo, HttpServletResponse response) {
        //生成token
        String token = getToken();
        //将token做key，用户id做值存入redis
        int redisMaxTime = 30;//30分钟
        redisTemplate.opsForValue().set("user:token" + token,userVo,redisMaxTime, TimeUnit.MINUTES);
        //生成刷新token
        String refreshToken = getToken();
        redisTemplate.opsForValue().set("user:refreshToken" + refreshToken,userVo,redisMaxTime *2,TimeUnit.MINUTES);

        //将token、refreshToken和name存入cookie
        int cookieMaxTime = 60 * 30; //30分钟
        CookieUtils.setCookie(response,"token",token,cookieMaxTime);
        CookieUtils.setCookie(response,"refreshToken",refreshToken,cookieMaxTime*2);
        CookieUtils.setCookie(response,"name", URLEncoder.encode(userVo.getName()),cookieMaxTime*2);


    }

    private String getToken() {
        return UUID.randomUUID().toString().replace("-","");
    }

    private Long refreshToken(HttpServletRequest request, HttpServletResponse response) {
        //从cookie中获取刷新token
        String refreshToken = CookieUtils.getCookie(request, "refreshToken");

        //从redis中根据刷新token获取用户信息
        Object userVoObj = redisTemplate.opsForValue().get("user:refreshToken" + refreshToken);
        if (userVoObj == null) {
            throw new YyghException(ResultCodeEnum.LOGIN_AUTH);
        }
        UserVo userVo =(UserVo) userVoObj;
        saveToken(userVo,response);
        return userVo.getUserId();
    }
}
