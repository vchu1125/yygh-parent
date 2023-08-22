package com.atguigu.yygh.user.controller.front;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Weizhu
 * @Date 2023/8/17 16:34
 * @注释
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/front/user/userInfo")
public class FrontUserInfoController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private AuthContextHolder authContextHolder;

    @ApiOperation("用户认证")
    @ApiImplicitParam(name = "userAuthVo",value = "用户实名认证对象",required = true)
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request,HttpServletResponse response){
        Long userId = authContextHolder.checkAuth(request,response);
        userInfoService.userAuth(userId,userAuthVo);
        return Result.ok();
    }

    @ApiOperation("获取认证信息")
    @GetMapping("auth/getUserInfo")
    public Result<UserInfo> getUserInfo(HttpServletRequest request, HttpServletResponse response){
        Long userId = authContextHolder.checkAuth(request,response);
        UserInfo userInfo = userInfoService.getUserInfoById(userId);
        return Result.ok(userInfo);
    }

}
