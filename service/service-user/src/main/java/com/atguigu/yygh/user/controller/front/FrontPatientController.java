package com.atguigu.yygh.user.controller.front;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/21 20:20
 * @注释
 */
@Api(tags = "就诊人管理")
@RestController
@RequestMapping("/front/user/patient")
public class FrontPatientController {
    @Resource
    private PatientService patientService;

    @Resource
    AuthContextHolder authContextHolder;

    @ApiOperation(value = "添加就诊人")
    @ApiImplicitParam(name = "patient", value = "就诊人对象", required = true)
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request, HttpServletResponse response) {
        //获取当前登录用户id
        Long userId = authContextHolder.checkAuth(request,response);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok().message("添加就诊人成功");
    }

    @ApiOperation(value = "修改就诊人")
    @ApiImplicitParam(name = "patient", value = "就诊人对象", required = true)
    @PutMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient, HttpServletRequest request, HttpServletResponse response) {
        authContextHolder.checkAuth(request,response);
        patientService.updateById(patient);
        return Result.ok().message("修改就诊人成功");
    }

    @ApiOperation(value = "根据id获取就诊人")
    @ApiImplicitParam(name = "id", value = "就诊人id", required = true)
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Long userId = authContextHolder.checkAuth(request, response);
        //加上userId的目的是为了防止横向越权
        Patient patient = patientService.getPatientById(id,userId);
        return Result.ok(patient);
    }

    @ApiOperation(value = "获取就诊人列表")
    @GetMapping("auth/findAll")
    public Result<List<Patient>> findAll(HttpServletRequest request, HttpServletResponse response) {
        Long userId = authContextHolder.checkAuth(request, response);
        List<Patient> list = patientService.findByUserId(userId);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据id删除就诊人")
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Long userId = authContextHolder.checkAuth(request, response);
        //加上userId的目的是为了防止横向越权
        patientService.removeById(id,userId);
        return Result.ok().message("删除成功");
    }
}
