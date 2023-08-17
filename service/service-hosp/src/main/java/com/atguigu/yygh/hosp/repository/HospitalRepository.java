package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/5 16:43
 * @注释
 */
public interface HospitalRepository extends MongoRepository<Hospital, ObjectId> {
    /**
     * 根据医院编号查询医院
     * @param hoscode
     * @return
     */
    Hospital findByHoscode(String hoscode);

    Page<Hospital> findByHosnameLike(String hosname, Pageable pageable);

    List<Hospital> findByHosnameLikeAndHostypeAndDistrictCodeAndStatus(String hosname, String hostype, String districtCode, Integer status, Sort sort);
}
