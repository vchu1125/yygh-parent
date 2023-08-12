package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/8 21:00
 * @注释
 */
public interface ScheduleService {
    /**
     * 上传排班接口
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询排班接口
     * @param page
     * @param limit
     * @param hoscode
     * @return
     */
    Page<Schedule> selectPage(int page, int limit, String hoscode);

    /**
     * 删除排班接口
     * @param hoscode
     * @param hosScheduleId
     */
    void remove(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号和科室编号查询排班信息
     * @param hoscode
     * @param depcode
     * @return
     */
    Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    /**
     * 根据医院编号、科室编号和工作日期查询排班信息
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    List<Schedule> getScheduleList(String hoscode, String depcode, String workDate);
}
