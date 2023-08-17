package com.atguigu.yygh.hosp.controller.front;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/15 11:08
 * @注释
 */
@Api(tags = "科室管理")
@RestController
@RequestMapping("/front/hosp/department")
public class FrontDepartmentController {
    @Resource
    private DepartmentService departmentService;

    @ApiOperation("医院挂号科室选择")
    @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true)
    @GetMapping("getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> getDeptList(@PathVariable(value = "hoscode") String hoscode){
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return Result.ok(deptTree);
    }
}
