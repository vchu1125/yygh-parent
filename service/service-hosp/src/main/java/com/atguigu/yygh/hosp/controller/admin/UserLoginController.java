package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author atguigu
 * @since 2021-10-30
 */
@Api(tags = "登录接口")
@RestController
@RequestMapping("/admin/hosp")
//@CrossOrigin  //跨域
public class UserLoginController {

    //http://localhost:9528/dev-api/vue-admin-template/user/login
    //{"code":20000,"data":{"token":"admin-token"}}
    //登录
    @PostMapping("user/login")
    public Result<Map> login() {

        Map<String, String> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }

//    http://localhost:9528/dev-api/vue-admin-template/user/info?token=admin-token
//    {"code":20000,"data":{"roles":["admin"],
//        "introduction":"I am a super administrator",
//                "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
//                "name":"Super Admin"}}
    //info
    @GetMapping("user/info")
    public Result<Map> info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles", new String[]{"admin"});
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Admin");
        return Result.ok(map);
    }

//    http://localhost:9528/dev-api/vue-admin-template/user/logout
//   {"code":20000,"data":"success"}
    @PostMapping("user/logout")
    public Result logout() {
        return Result.ok();
    }
}

