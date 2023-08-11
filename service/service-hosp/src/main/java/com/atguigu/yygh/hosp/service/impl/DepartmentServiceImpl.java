package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Weizhu
 * @Date 2023/8/8 18:57
 * @注释
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentRepository departmentRepository;
    @Override
    public void save(Map<String, Object> paramMap) {
        //把paramMap转换成Department对象
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);
        //根据医院编码和科室编号查询科室信息是否已经添加
        Department existsDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        //如果存在，则进行修改，设置id
        if (existsDepartment != null){
            department.setId(existsDepartment.getId());
            departmentRepository.save(department);
        }else {
            //如果不存在，则进行保存
            departmentRepository.save(department);
        }
    }

    @Override
    public Object getByHoscode(String hoscode) {
        return departmentRepository.getDepartmentByHoscode(hoscode);
    }

    @Override
    public Page<Department> selectPage(int page, int limit, String hoscode) {
        //设置排序规则
        Sort sort = Sort.by(Sort.Direction.ASC, "hoscode");
        //设置分页参数
        Pageable pageable = PageRequest.of(page-1, limit, sort);
        //创建查询对象
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);

        return departmentRepository.findAll(example,pageable);
    }

    @Override
    public void remove(String depcode, String hoscode) {
        //根据医院编码和科室编码查询科室信息
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        //如果存在，则进行删除
        if (department != null){
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建集合result用于组装返回组装的数据
        List<DepartmentVo> result = new ArrayList<>();
        //根据hoscode查询所有的科室
        List<Department> departmentList = departmentRepository.findByHoscode(hoscode);
        //根据bigcode给科室列表分组,封装到map<String,List<DepartmentVo>>
        Map<String,List<Department>> departmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map，封装大科室
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            //大科室编码
            String bigcode = entry.getKey();
            //大科室的对应的所有数据
            List<Department> subList = entry.getValue();

            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(subList.get(0).getDepname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : subList) {
                DepartmentVo subDepartmentVo = new DepartmentVo();
                subDepartmentVo.setDepcode(department.getDepcode());
                subDepartmentVo.setDepname(department.getDepname());
                children.add(subDepartmentVo);
            }
            //把小科室放到大科室的children
            departmentVo.setChildren(children);
            //组装最终的result
            result.add(departmentVo);
        }
        return result;
    }
}
