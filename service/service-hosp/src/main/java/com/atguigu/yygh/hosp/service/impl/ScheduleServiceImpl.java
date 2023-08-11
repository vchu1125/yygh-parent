package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/8 21:00
 * @注释
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleRepository scheduleRepository;
    @Override
    public void save(Map<String, Object> paramMap) {
        //把paramMap转换成Schedule对象
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);

        //根据医院编码和排班编码hosScheduleId查询是否有这个信息
        Schedule ExistsSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getDepcode());
        //如果存在，则进行修改，设置id
        if (ExistsSchedule != null){
            schedule.setId(ExistsSchedule.getId());
            scheduleRepository.save(schedule);
        }else {
            //如果不存在，则进行保存
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> selectPage(int page, int limit, String hoscode) {
        //排序、分页
        Sort sort = Sort.by(Sort.Direction.DESC, "workDate");
        Pageable pageable = PageRequest.of(page-1,limit,sort);
        //创建查询对象
        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        Example<Schedule> example = Example.of(schedule);

        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        //根据医院编码和排班编码查询判断是否存在数据
        Schedule existsSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (existsSchedule != null){
            scheduleRepository.deleteById(existsSchedule.getId());
        }
    }
}
