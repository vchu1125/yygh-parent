package com.atguigu.yygh.cmn.controller.inner;

import com.atguigu.yygh.cmn.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Weizhu
 * @Date 2023/8/9 21:11
 * @注释
 */
@Api(tags = "地区")
@RestController
@RequestMapping("/inner/cmn/region")
public class InnerRegionController {
    @Resource
    private RegionService regionService;

    @ApiOperation(value = "根据地区编码获取地区名称")
    @ApiImplicitParam(name = "code",value = "地区编码",required = true)
    @GetMapping("/getName/{code}")
    public String getName(@PathVariable("code") String code){
        String name = regionService.getNameByCode(code);
        return name;
    }
}
