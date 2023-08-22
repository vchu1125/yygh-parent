package com.atguigu.yygh.oss.controller.front;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/17 14:35
 * @注释
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/front/oss/file")
public class FrontFileController {
    @Resource
    private FileService fileService;

    @Resource
    private AuthContextHolder authContextHolder;

    @ApiOperation("文件上传")
    @ApiImplicitParam(name = "file",value = "上传文件",required = true)
    @PostMapping("auth/upload")
    public Result<Map<String,String>> upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //授权校验
        authContextHolder.checkAuth(request,response);
        Map<String,String> map = fileService.upload(file);
        return Result.ok(map).message("上传成功");
    }
}
