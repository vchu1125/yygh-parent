package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Department;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/8 18:46
 * @注释
 */
public interface DepartmentRepository extends MongoRepository<Department, ObjectId> {
    //根据医院编号和科室编号查询
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

    List<Department> getDepartmentByHoscode(String hoscode);

    List<Department> findByHoscode(String hoscode);
}
