package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/8 18:56
 * @注释
 */
public interface DepartmentService {
    /**
     * 上传科室接口
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询科室接口
     * @param hoscode
     * @return
     */
    Object getByHoscode(String hoscode);

    /**
     * 查询科室接口
     * @param page
     * @param limit
     * @param hoscode
     * @return
     */
    Page<Department> selectPage(int page, int limit, String hoscode);

    /**
     * 根据医院编码和科室编码删除科室
     * @param depcode
     * @param hoscode
     */
    void remove(String depcode, String hoscode);

    List<DepartmentVo> findDeptTree(String hoscode);
}
