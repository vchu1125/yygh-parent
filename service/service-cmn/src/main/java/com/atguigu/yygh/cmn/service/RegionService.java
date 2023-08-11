package com.atguigu.yygh.cmn.service;


import com.atguigu.yygh.model.cmn.Region;
import com.atguigu.yygh.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
public interface RegionService extends IService<Region> {

    /**
     * 根据父节点id查询子节点地区信息
     * @param parentCode
     * @return
     */
    List<Region> findRegionByParentCode(String parentCode);

    /**
     * 测试 @CachePut
     * 将方法的返回值放到缓存中
     * @param region
     * @return
     */
    Region saveRegionWithCacheManager(Region region);

    /**
     * 测试 @CacheEvict
     * @param id
     */
    void deleteRegionWithCacheManager(Long id);


    List<RegionExcelVo> findRegionExcelVoList();

    void importData(MultipartFile file) throws IOException;

    /**
     * 根据地区编码获取地区名称
     * @param code
     * @return
     */
    String getNameByCode(String code);
}
