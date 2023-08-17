package com.atguigu.yygh.hosp.controller.front;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/14 9:24
 * @注释
 */
@Api(tags = "医院接口")
@RestController
@RequestMapping("/front/hosp/hospital")
public class FrontHospitalController {
    @Resource
    private HospitalService hospitalService;

    @ApiOperation("根据医院名称、级别、和区域查询医院列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hosname",value = "医院名称"),
            @ApiImplicitParam(name = "hostype",value = "医院类型"),
            @ApiImplicitParam(name = "districtCode",value = "医院地区")
    })
    @GetMapping("list")
    public Result<List<Hospital>> getHospitalsList(String hosname,String hostype,String districtCode){
        List<Hospital> hospitalList = hospitalService.selectList(hosname,hostype,districtCode);
        return Result.ok(hospitalList);
    }

    @ApiOperation("预约挂号详情")
    @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true)
    @GetMapping("show/{hoscode}")
    public Result<Hospital> show(@PathVariable(value = "hoscode") String hoscode){
        Hospital hospital = hospitalService.showHospital(hoscode);
        return Result.ok(hospital);
    }

}
