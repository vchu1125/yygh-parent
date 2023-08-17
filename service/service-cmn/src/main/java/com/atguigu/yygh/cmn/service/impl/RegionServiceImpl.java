package com.atguigu.yygh.cmn.service.impl;


import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.RegionExcelListener;
import com.atguigu.yygh.cmn.mapper.RegionMapper;
import com.atguigu.yygh.cmn.service.RegionService;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.cmn.Region;
import com.atguigu.yygh.vo.cmn.RegionExcelVo;
import com.atguigu.yygh.vo.cmn.RegionVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @Cacheable：
     * 在方法执行前查看是否有缓存对应的数据，
     * 如果有直接返回数据，如果没有，则调用方法获取数据返回，并缓存起来。
     *
     * value：缓存的名字
     * key：缓存的key
     * unless：条件符合则不缓存，是对出参进行判断，出参用#result表示
     *
     * @param parentCode
     * @return
     */
    @Override
    @Cacheable(value = "regionList",key = "#parentCode",unless = "#result.size() == 0")
    public List<Region> findRegionByParentCode(String parentCode) {

        //先判断 redis中是否有regionList
//        List<Region> regionList = null;
//        regionList = (List<Region>)redisTemplate.opsForValue().get("regionList:" + parentCode);
//        try {
//            if(regionList != null){
//                return regionList;
//            }
//        } catch (Exception e) {
//            log.error("redis服务器异常：get regionList");
//        }

        LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Region::getParentCode,parentCode);
        List<Region> regionList = baseMapper.selectList(queryWrapper);
        //遍历regionList，判断level是否小于3，小于三说明有子节点，hasChildren设置为true
        regionList.forEach(region -> {
           Boolean hasChildren = region.getLevel().intValue() < 3 ? true : false;
           region.setHasChildren(hasChildren);
        });
//        try {
//            //将数据存入redis
//            redisTemplate.opsForValue().set("regionList:" + parentCode,regionList,5, TimeUnit.MINUTES);
//        } catch (Exception e) {
//            log.error("redis服务器异常：get regionList");
//        }
        return regionList;
    }


    /**
     * 作用：将方法的返回值放到缓存中
     * @param region
     * @return
     */
    @Override
    @CachePut(value = "regionTest",key = "#region.id")
    public Region saveRegionWithCacheManager(Region region) {
        baseMapper.insert(region);
        return region;
    }

    /**
     * 作用：执行方法体，删除缓存
     * @param id
     */
    @Override
    @CacheEvict(value = "regionTest",key = "#id")
    public void deleteRegionWithCacheManager(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<RegionExcelVo> findRegionExcelVoList() {
        List<Region> regionList = this.list();
        List<RegionExcelVo> regionExcelVoList = new ArrayList<>();
        regionList.forEach(region -> {
            RegionExcelVo regionExcelVo = new RegionExcelVo();
            BeanUtils.copyProperties(region,regionExcelVo);
            regionExcelVoList.add(regionExcelVo);
        });

        return regionExcelVoList;
    }

    @Override
    public void importData(MultipartFile file){
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            EasyExcel.read(file.getInputStream(), RegionExcelVo.class, new RegionExcelListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            throw new YyghException(ResultCodeEnum.IMPORT_DATA_ERROR,e);
        }
    }

    @Override
    public String getNameByCode(String code) {
        //根据code查询地区
        LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Region::getCode,code);
        Region region = baseMapper.selectOne(queryWrapper);
        //判断region是否为空
        if(region == null){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        return region.getName();
    }

    @Override
    public List<RegionVo> findRegionListByParentCode(String parentCode) {
        LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Region::getParentCode,parentCode);
        //创建RegionVo对象，优化返回字段冗余字段太多的问题
        List<RegionVo> regionVoList = new ArrayList<>();
        List<Region> regionList = baseMapper.selectList(lambdaQueryWrapper);
        regionList.stream().forEach(region -> {
            RegionVo regionVo = new RegionVo();
            regionVo.setCode(region.getCode());
            regionVo.setName(region.getName());
            regionVoList.add(regionVo);
        });
        return regionVoList;
    }
}
