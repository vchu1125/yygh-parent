package com.atguigu.yygh.user.controller.front;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.CookieUtils;
import com.atguigu.yygh.user.utils.ConstantProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author Weizhu
 * @Date 2023/8/15 18:49
 * @注释 https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
 */
@Api(tags = "微信扫码登录")
@Controller
@RequestMapping("front/user/wx")
@Slf4j
public class FrontWxController {
    @Resource
    private ConstantProperties constantProperties;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("login")
    public String login(HttpSession session, HttpServletResponse response) {

        try {
            //得到授权临时票据code和state参数
            StringBuffer baseUrl = new StringBuffer()
                    .append("https://open.weixin.qq.com/connect/qrconnect?")
                    .append("appid=%s")
                    .append("&redirect_uri=%s")
                    .append("&response_type=code")
                    .append("&scope=snsapi_login")
                    .append("&state=%s")
                    .append("#wechat_redirect");


            //处理redirectUri
            String redirectUri = URLEncoder.encode(constantProperties.getRedirectUri(), "UTF-8");

            //处理state:生成随机数，伪随机数，存入session
            //ThreadLocalRandom解决了Random在高并发环境下随机数生成的性能问题
            long nonce = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
            String state = Long.toHexString(nonce);
            log.info("生成 state ={} ", state);

            //生成redis中的key： 随机数:wx_open_state
            String stateKey = UUID.randomUUID().toString();
            //redis中的value：state
            redisTemplate.opsForValue().set(stateKey +":wx_open_state",state);
            //将随机数存入cookie   statekey:随机数
            CookieUtils.setCookie(response,"stateKey",stateKey);

            //session.setAttribute("wx_open_state", state);
            String qrcodeUrl = String.format(baseUrl.toString(),
                    constantProperties.getAppId(),
                    redirectUri,
                    state);

            return "redirect:"+qrcodeUrl;
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.URL_ENCODE_ERROR, e);
        }
    }

}
