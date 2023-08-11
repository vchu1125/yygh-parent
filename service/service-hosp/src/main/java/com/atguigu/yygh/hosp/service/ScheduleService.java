package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;

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
}
