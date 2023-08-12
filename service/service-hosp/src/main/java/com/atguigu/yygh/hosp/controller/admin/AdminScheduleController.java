package com.atguigu.yygh.hosp.controller.admin;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/11 18:41
 * @注释
 */
@Api(tags = "排班管理")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class AdminScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @ApiOperation("查询排班规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码",required = true),
            @ApiImplicitParam(name = "limit",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true),
            @ApiImplicitParam(name = "depcode",value = "科室编码",required = true)
    })
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Map<String,Object>> getScheduleRule(@PathVariable Long page,
                                       @PathVariable Long limit,
                                       @PathVariable String hoscode,
                                       @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    @ApiOperation("获取排班详情列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true),
            @ApiImplicitParam(name = "depcode",value = "科室编码",required = true),
            @ApiImplicitParam(name = "workDate",value = "排班日期",required = true)
    })
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result<List<Schedule>> getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getScheduleList(hoscode,depcode,workDate);
        return  Result.ok(list);
    }
}
