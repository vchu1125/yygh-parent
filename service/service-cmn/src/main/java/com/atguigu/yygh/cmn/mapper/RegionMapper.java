package com.atguigu.yygh.cmn.mapper;


import com.atguigu.yygh.model.cmn.Region;
import com.atguigu.yygh.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
@Mapper
public interface RegionMapper extends BaseMapper<Region> {

    /**
     * 根据RegionExcelVo列表批量保存数据
     * @param cachedDataList
     */
    void saveBatch(@Param("list") List<RegionExcelVo> cachedDataList);
}
