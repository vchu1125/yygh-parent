package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.cmn.client.RegionFeignClient;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.enums.DictTypeEnum;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/7 10:14
 * @注释
 */
@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {
    @Resource
    private HospitalRepository hospitalRepository;
    @Resource
    private DictFeignClient dictFeignClient;
    @Resource
    private RegionFeignClient regionFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        //把paramMap转换成Hospital对象
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Hospital.class);
        //根据医院编码查询医院信息是否已经添加
        Hospital hoscode = hospitalRepository.findByHoscode(hospital.getHoscode());
        if (hoscode != null) {
            //如果存在，则进行修改，设置id
            hospital.setId(hoscode.getId());
            hospital.setStatus(hoscode.getStatus());
            hospitalRepository.save(hospital);
        } else {
            //如果不存在，则进行添加
            hospital.setStatus(1);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Object getByHoscode(String hoscode) {
        return hospitalRepository.findByHoscode(hoscode);
    }

    /**
     *封装hositpal对象
     * @param hospital
     * @return
     */
    private Hospital setHospital(Hospital hospital){
        //获取医院等级名称
        String hostypeString = dictFeignClient.getName(DictTypeEnum.HOSTYPE.getDictTypeId(), hospital.getHostype());
        //获取省份名称
        String provinceName = regionFeignClient.getName(hospital.getProvinceCode());
        //获取城市名称
        String cityName = regionFeignClient.getName(hospital.getCityCode());
        //获取区域名称
        String DistrictName = regionFeignClient.getName(hospital.getDistrictCode());
        //判断直辖市省市同名,去掉重名
        if (provinceName.equals(cityName)){
            cityName = "";
        }
        //拼接详细地址得到fullAddress
        String fullAddress = provinceName + cityName + DistrictName + hospital.getAddress();
        //组装数据到param
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",fullAddress);
        return hospital;
    }


    /**
     * 医院列表(条件查询分页)
     * @param page
     * @param limit
     * @param hosname
     * @return
     */
    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, String hosname) {
        //排序
        Sort sort =  Sort.by(Sort.Direction.DESC,"hoscode");
        //分页参数
        Pageable pageable =PageRequest.of(page-1,limit,sort);

        Page<Hospital> pages = null;
        //判断hosname是否为空
        if (StringUtils.isEmpty(hosname)){
            //查询分页数据
            pages = hospitalRepository.findAll(pageable);
        }else {
            //根据医院名称进行模糊查询
            pages = hospitalRepository.findByHosnameLike(hosname,pageable);
        }
        //获取医院列表
        log.warn("begin******************************");
        List<Hospital> hospitalList = pages.getContent();
        hospitalList.stream().forEach(hospital -> {
            this.setHospital(hospital);
        });
        log.warn("end******************************");
        return pages;
    }

    @Override
    public void updateStatus(String hoscode, Integer status) {
        if (status == 0 || status == 1){
            //根据医院编码查询医院对象
            Hospital hospital = hospitalRepository.findByHoscode(hoscode);
            hospital.setStatus(status);
            hospitalRepository.save(hospital);
            return;
        }
        throw new YyghException(ResultCodeEnum.PARAM_ERROR);
    }

    @Override
    public Hospital showHospital(String hoscode) {
        Hospital hospital = hospitalRepository.findByHoscode(hoscode);
        this.setHospital(hospital);
        return hospital;
    }

    @Override
    public List<Hospital> selectList(String hosname, String hostype, String districtCode) {
        Sort sort = Sort.by(Sort.Direction.ASC, "hoscode");
        //这个方法，参数为null的时候需要做判断要不会报错
        // List<Hospital> hospitalList = hospitalRepository.findByHosnameLikeAndHostypeAndDistrictCodeAndStatus(hosname,hostype,districtCode,1,sort);
        //组装查询条件模糊
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("hosname", ExampleMatcher.GenericPropertyMatchers.contains())//模糊查询
                .withMatcher("hostype", ExampleMatcher.GenericPropertyMatchers.exact())//精确查询
                .withMatcher("districtCode", ExampleMatcher.GenericPropertyMatchers.exact());//精确查询
        //创建查询对象
        Hospital hospital = new Hospital();
        hospital.setStatus(1);//已上线
        hospital.setHosname(hosname);
        hospital.setHostype(hostype);
        hospital.setDistrictCode(districtCode);

        Example<Hospital> example =Example.of(hospital,matcher);
        //执行查询
        List<Hospital> hospitalList = hospitalRepository.findAll(example, sort);
        //封装医院等级数据
        hospitalList.forEach(this::setHospital);

        return hospitalList;
    }

}
