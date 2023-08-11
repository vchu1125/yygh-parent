package com.atguigu.yygh.hosp.service;


import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-07-24
 */
public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);

    HospitalSet getByHoscode(String hoscode);
}
