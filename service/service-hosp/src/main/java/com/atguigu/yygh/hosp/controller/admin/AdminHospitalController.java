package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Weizhu
 * @Date 2023/8/9 18:52
 * @注释
 */
@Api(tags = "医院管理")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class AdminHospitalController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "获取医院分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "limit",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "hosname",value = "查询字符串")
    })
    @GetMapping("/{page}/{limit}")
    public Result<Page<Hospital>> hospitalPage(@PathVariable Integer page, @PathVariable Integer limit,String hosname){
        Page<Hospital> hospitalsList = hospitalService.selectPage(page,limit,hosname);
        return Result.ok(hospitalsList);
    }


    @ApiOperation(value = "更新医院上线状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true),
            @ApiImplicitParam(name = "status",value = "医院状态",required = true)
    })
    @GetMapping("updateStatus/{hoscode}/{status}")
    public Result updateStatus(@PathVariable String hoscode,@PathVariable Integer status){
        hospitalService.updateStatus(hoscode,status);
        return Result.ok();
    }

    @ApiOperation("查看医院详情")
    @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true)
    @GetMapping("show/{hoscode}")
    public Result showHospital(@PathVariable String hoscode){
        Hospital hospital = hospitalService.showHospital(hoscode);
        return Result.ok(hospital);
    }





}
