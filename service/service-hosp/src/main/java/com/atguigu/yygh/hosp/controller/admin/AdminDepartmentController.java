package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/10 22:37
 * @注释
 */
@Api(tags = "科室管理")
@RestController
@RequestMapping("/admin/hosp/department")
public class AdminDepartmentController {

    @Resource
    private DepartmentService departmentService;

    @ApiOperation("查询所有科室列表")
    @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true)
    @GetMapping("getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> departmentVoList = departmentService.findDeptTree(hoscode);
        return Result.ok(departmentVoList);
    }
}
