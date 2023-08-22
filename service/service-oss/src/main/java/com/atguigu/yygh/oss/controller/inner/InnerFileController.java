package com.atguigu.yygh.oss.controller.inner;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.yygh.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Weizhu
 * @Date 2023/8/18 18:22
 * @注释
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/inner/oss/file")
public class InnerFileController {

    @Resource
    private FileService fileService;

    @ApiOperation("获取图片预览url")
    @ApiImplicitParam(name = "objectName",value = "文件名",required = true)
    @GetMapping("getPreviewUrl")
    public String getPreviewUrl(@RequestParam String objectName) {
        return fileService.getPreviewUrl(objectName);
    }
}
