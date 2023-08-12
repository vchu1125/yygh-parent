package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.hosp.util.DateUtil;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleRuleVo;
import org.joda.time.DateTime;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Resource
    private MongoTemplate mongoTemplate;
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

    @Override
    public Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode) {
        //组装查询条件
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation aggregation = Aggregation.newAggregation(
                //过滤
                Aggregation.match(criteria),
                //分组
                Aggregation.group("workDate").first("workDate").as("workDate")
                        //聚合
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //排序
                Aggregation.sort(Sort.Direction.ASC,"workDate"),
                //分页
                Aggregation.skip((page-1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<ScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, ScheduleRuleVo.class);
        List<ScheduleRuleVo> scheduleRuleVoList = aggregationResults.getMappedResults();

        //根据日期计算星期
        scheduleRuleVoList.stream().forEach(scheduleRuleVo -> {
            Date workDate = scheduleRuleVo.getWorkDate();
            String dayOfWeek = DateUtil.getDayOfWeek(new DateTime(workDate));
            scheduleRuleVo.setDayOfWeek(dayOfWeek);
        });


        //获取分页前的总记录数
        Aggregation aggregation1 = Aggregation.newAggregation(
                //过滤
                Aggregation.match(criteria),
                //分组
                Aggregation.group("workDate"));
        AggregationResults<ScheduleRuleVo> aggregationTotal = mongoTemplate.aggregate(aggregation1, Schedule.class, ScheduleRuleVo.class);
        int total = aggregationTotal.getMappedResults().size();

        HashMap<String, Object> map = new HashMap<>();
        map.put("list",scheduleRuleVoList);
        map.put("total",total);

        return map;
    }

    @Override
    public List<Schedule> getScheduleList(String hoscode, String depcode, String workDate) {
        List<Schedule> scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workDate));
        return scheduleList;
    }
}
