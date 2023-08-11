package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.HttpRequestHelper;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/5 16:53
 * @注释
 */
@Api("医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Resource
    private HospitalService hospitalService;
    @Resource
    private HospitalSetService hospitalSetService;
    @Resource
    DepartmentService departmentService;
    @Resource
    private ScheduleService scheduleService;

    //上传医院接口
    @ApiOperation(value = "上传医院基本信息")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //调用service的方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

    //查询医院接口
    @ApiOperation(value = "查询医院")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //调用service的方法
        return Result.ok(hospitalService.getByHoscode(hoscode));
    }

    //上传科室接口
    @ApiOperation(value = "上传科室信息")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //调用service的方法
        departmentService.save(paramMap);
        return Result.ok();
    }

    //查询科室接口
    @ApiOperation(value = "查询科室分页数据")
    @PostMapping("department/list")
    public Result getDepartmentPage(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //获取page、limit 使用Integer.parseInt()将字符串转换为整数
        int page = Integer.parseInt((String) paramMap.get("page"));
        int limit = Integer.parseInt((String) paramMap.get("limit"));
        //调用service的方法
        Page<Department> departmentPage = departmentService.selectPage(page,limit,hoscode);

        return Result.ok(departmentPage);
    }

    //删除科室接口
    @ApiOperation(value = "删除科室")
    @PostMapping("department/remove")
    public Result deleteDepartment(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //获取depcode
        String depcode =(String) paramMap.get("depcode");

        //调用service的方法
        departmentService.remove(depcode,hoscode);

        return Result.ok();
    }

    //上传排班接口
    @ApiOperation(value = "上传排班信息")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //调用service的方法
        scheduleService.save(paramMap);
        return Result.ok();
    }

    //查询排班接口
    @ApiOperation(value = "查询排班")
    @PostMapping("schedule/list")
    public Result getSchedulePage(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证签名
        String hoscode =(String) paramMap.get("hoscode");
        //根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);

        //获取page、limit 使用Integer.parseInt()将字符串转换为整数
        int page = Integer.parseInt((String) paramMap.get("page"));
        int limit = Integer.parseInt((String) paramMap.get("limit"));
        //调用service的方法
        Page<Schedule> schedulePage = scheduleService.selectPage(page,limit,hoscode);

        return Result.ok(schedulePage);
    }

    //删除排班信息接口
    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result deleteSchedule(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode =(String) paramMap.get("hoscode");
        //根据医院编码查询数据库，查询验签
        String signKey = hospitalSetService.getSignKey(hoscode);
        HttpRequestHelper.checkSign(paramMap,signKey);
        //获取排班编号
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hoscode,hosScheduleId);

        return Result.ok();
    }

}
