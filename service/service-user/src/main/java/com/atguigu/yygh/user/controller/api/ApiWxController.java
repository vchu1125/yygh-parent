package com.atguigu.yygh.user.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.common.utils.CookieUtils;
import com.atguigu.yygh.common.utils.HttpUtil;
import com.atguigu.yygh.enums.UserStatusEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.ConstantProperties;
import com.atguigu.yygh.vo.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author Weizhu
 * @Date 2023/8/15 20:18
 * @注释
 */
@Api(tags = "微信扫码登录回调")
@Controller
@RequestMapping("/api/user/wx")
@Slf4j
public class ApiWxController {
    @Resource
    private ConstantProperties constantProperties;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AuthContextHolder  authContextHolder;
    //登录回调
    @ApiOperation("微信登录回调")
    @GetMapping("callback")
    public String callback(String code, String state, HttpServletRequest request, HttpServletResponse response){
        try {
            //String sessionState =(String) session.getAttribute("wx_open_state");
            //将随机数从cookie中取出statekey的值
            String stateKey = CookieUtils.getCookie(request, "stateKey");
            //从redis中获取： 随机数:wx_open_state   的值
            String sessionState = (String)redisTemplate.opsForValue().get(stateKey + ":wx_open_state");

            if(StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
                throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            //和请求中的state进行比较
            if (!state.equals(sessionState)){
                throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            log.info("参数合法");

            //通过code参数加上AppID和AppSecret等，通过API换取access_token；
            String appId = constantProperties.getAppId();
            String appSecret = constantProperties.getAppSecret();
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl, appId, appSecret, code);
            //使用httpclient发送请求
            byte[] respData = HttpUtil.doGet(accessTokenUrl);
            String result = new String(respData);
            JSONObject jsonObject = JSONObject.parseObject(result);
            //校验是否返回成功
            if(jsonObject.get("errcode") != null){
                log.error("获取access_token失败：" + result);
                throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }
            String accessToken = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");
            log.info("accessToken:{}",accessToken);
            log.info("openid:{}",openid);

            //根据access_token获取微信用户的基本信息
            //先根据openid进行数据库查询
            UserInfo userInfo = userInfoService.getByOpenId(openid);
            if (userInfo == null){
                //用户不存在，进行注册
                log.info("注册用户");
                //使用access_token换取受保护的资源，微信个人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //使用httpclient发送请求
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                byte[] respDataUser = HttpUtil.doGet(userInfoUrl);
                String resultUserInfo = new String(respDataUser);
                //将字符串转换为对象，用于取数据
                JSONObject userInfoObject = JSONObject.parseObject(resultUserInfo);
                if (userInfoObject.getString("errcode") != null){
                    log.error("获取用户信息失败: " + userInfoObject.getString("errcode") + userInfoObject.getString("errmsg"));
                    throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
                }
                //解析用户信息
                //获取昵称
                String nickname = userInfoObject.getString("nickname");
                //用户注册
                userInfo = new UserInfo();
                userInfo.setOpenid(openid);
                userInfo.setNickName(nickname);
                userInfo.setStatus(UserStatusEnum.NORMAL.getStatus());
                userInfoService.save(userInfo);
            }else {
                //用户已存在，判断用户状态
                log.info("判断用户是否被禁用");
                if (userInfo.getStatus() == UserStatusEnum.LOCK.getStatus()){
                    log.error("用户已经被禁用");
                    throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
                }
            }
            //获取用户名，如果没有用户名（未实名认证），则获取昵称
            String name = userInfo.getName();
            if (StringUtils.isEmpty(name)){
                name = userInfo.getNickName();
            }
            //生成token
            String token = UUID.randomUUID().toString().replace("-","");
            //将token做成key，用户id做值存入redis 设置30分钟过期时间
            redisTemplate.opsForValue().set("user:token:" + token,userInfo.getId(),30, TimeUnit.MINUTES);
            //将token和name存入cookie
            //使用CookieUtils 对名称进行url编码防止中文报错
            int cookieMaxTime = 60 * 30; //30分钟
            CookieUtils.setCookie(response,"token",token,cookieMaxTime);
            CookieUtils.setCookie(response,"name", URLEncoder.encode(name),cookieMaxTime);

            UserVo userVo = new UserVo();
            userVo.setName(name);
            userVo.setUserId(userInfo.getId());
            authContextHolder.saveToken(userVo,response);

            return "redirect:" + constantProperties.getSytBaseUrl();
        } catch (YyghException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return "redirect:"+constantProperties.getSytBaseUrl()+"?code=201&message=" +URLEncoder.encode(e.getMsg());
        }catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return "redirect:"+constantProperties.getSytBaseUrl()+"?code=201&message=" +URLEncoder.encode("登录失败");
        }
    }


    @ApiOperation("callback1")
    @GetMapping("callback1")
    public String callback1(HttpServletRequest request, HttpServletResponse response) {
        //String sessionState =(String) session.getAttribute("wx_open_state");
        //将随机数从cookie中取出statekey的值
        String stateKey = CookieUtils.getCookie(request, "stateKey");
        //从redis中获取： 随机数:wx_open_state   的值
        String sessionState = (String) redisTemplate.opsForValue().get(stateKey + ":wx_open_state");

        log.info(sessionState);
        log.info("参数合法");
        return null;
    }
}
