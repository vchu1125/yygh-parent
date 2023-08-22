package com.atguigu.yygh.user.service;


import com.atguigu.yygh.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-15
 */
public interface PatientService extends IService<Patient> {

    // 根据就诊人id获取就诊人信息
    Patient getPatientById(Long id, Long userId);

    // 获取就诊人列表
    List<Patient> findByUserId(Long userId);

    // 删除就诊人
    void removeById(Long id, Long userId);
}
