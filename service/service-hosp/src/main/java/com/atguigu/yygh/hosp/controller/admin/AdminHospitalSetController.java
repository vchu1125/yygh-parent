package com.atguigu.yygh.hosp.controller.admin;


import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-07-24
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class AdminHospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;
    //查询所有医院设置信息
    @GetMapping("/findAll")
    public List<HospitalSet> findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }
    //根据id查询医院设置信息
    @GetMapping("/findById/{id}")
    public HospitalSet findById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return hospitalSet;
    }

    //根据id删除医院设置
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id){
        hospitalSetService.removeById(id);
    }

    //新增医院设置信息
    @PostMapping("/insert")
    public void insert(@RequestBody HospitalSet hospitalSet){
        hospitalSetService.save(hospitalSet);
    }

    //修改医院设置信息
    @PutMapping("/update")
    public void update(@RequestBody HospitalSet hospitalSet){
        hospitalSetService.update(hospitalSet,null);
    }

}

