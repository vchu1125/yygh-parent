package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/5 16:50
 * @注释
 */
public interface HospitalService {

    /**
     * 上传医院接口
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询医院接口
     * @param hoscode
     * @return
     */
    Object getByHoscode(String hoscode);

    //分页查询
    Page<Hospital> selectPage(Integer page, Integer limit, String hosname);

    //更新医院上线状态
    void updateStatus(String hoscode, Integer status);

    //查看医院详情
    Hospital showHospital(String hoscode);
}
