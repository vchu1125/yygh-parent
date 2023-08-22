package com.atguigu.yygh.user.controller.admin;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/21 21:14
 * @注释
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user/userInfo")
public class AdminUserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "用户分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "limit",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "userInfoQueryVo",value = "查询对象",required = false)
    })
    @GetMapping("{page}/{limit}")
    public Result<IPage<UserInfo>> list(@PathVariable Integer page,
                                        @PathVariable Integer limit,
                                        UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pageModel = userInfoService.selectPage(pageParam,userInfoQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "用户锁定和解锁")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "status",value = "用户状态（0：锁定 1：解锁）",required = true)
    })
    @PutMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId,
                       @PathVariable Integer status) {
        boolean result = userInfoService.lock(userId,status);
        if (result){
            return Result.ok().message("操作成功");
        }else {
            return Result.fail().message("操作失败");
        }
    }

    @ApiOperation(value = "用户详情")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true)
    @GetMapping("show/{userId}")
    public Result<Map<String,Object>> show(@PathVariable Long userId) {
        Map<String,Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }

    @ApiOperation(value = "认证审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "authStatus",value = "认证状态（2：认证成功 -1：认证失败）",required = true)
    })
    @PutMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId,
                           @PathVariable Integer authStatus) {
        boolean result = userInfoService.approval(userId,authStatus);
        if (result){
            return Result.ok().message("操作成功");
        }else {
            return Result.fail().message("操作失败");
        }
    }
}
