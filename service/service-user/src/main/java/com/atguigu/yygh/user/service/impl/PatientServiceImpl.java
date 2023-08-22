package com.atguigu.yygh.user.service.impl;


import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.cmn.client.RegionFeignClient;
import com.atguigu.yygh.enums.DictTypeEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-15
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    @Resource
    private DictFeignClient dictFeignClient;

    @Resource
    private RegionFeignClient regionFeignClient;

    @Override
    public Patient getPatientById(Long id, Long userId) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getId,id).eq(Patient::getUserId,userId);
        Patient patient = baseMapper.selectOne(queryWrapper);
        return this.packPatient(patient);
    }

    @Override
    public List<Patient> findByUserId(Long userId) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getUserId,userId);
        List<Patient> patientList = baseMapper.selectList(queryWrapper);
        patientList.stream().forEach(item -> {
            item.getParam().put("expenseMethod",item.getIsInsure() == 0 ? "自费" : "医保");
        });
        return patientList;
    }

    @Override
    public void removeById(Long id, Long userId) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getId,id).eq(Patient::getUserId,userId);
        baseMapper.delete(queryWrapper);
    }

    // 封装Patient对象里面其他参数
    private Patient packPatient(Patient patient) {
        // 根据证件类型编码获取证件类型具体值
        String certificatesTypeString = dictFeignClient.getName(DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(), patient.getCertificatesType());
        // 获取联系人证件类型
        String contactsCertificatesTypeString = dictFeignClient.getName(DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(), patient.getContactsCertificatesType());
        // 获取省市区
        String provinceString = regionFeignClient.getName(patient.getProvinceCode());
        String cityString = regionFeignClient.getName(patient.getCityCode());
        String districtString = regionFeignClient.getName(patient.getDistrictCode());
        // 封装参数
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }
}
