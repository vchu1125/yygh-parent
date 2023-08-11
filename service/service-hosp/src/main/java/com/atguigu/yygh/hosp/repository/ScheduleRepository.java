package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Schedule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author Weizhu
 * @Date 2023/8/8 21:02
 * @注释
 */
public interface ScheduleRepository extends MongoRepository<Schedule, ObjectId> {
    //根据医院编码和排班编码hosScheduleId查询是否有这个信息
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode,String depcode);
}
