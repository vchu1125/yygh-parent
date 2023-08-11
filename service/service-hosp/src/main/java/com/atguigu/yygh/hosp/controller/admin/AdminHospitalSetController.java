package com.atguigu.yygh.hosp.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
@Api(tags = "医院设置")
//@CrossOrigin //跨域
public class AdminHospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询所有医院设置信息
    @ApiOperation(value = "查询所有医院设置信息")
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }
    //根据id查询医院设置信息
    @ApiOperation(value = "根据id查询医院设置信息")
    @GetMapping("/findById/{id}")
    public Result<HospitalSet> findById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //根据id删除医院设置
    @ApiOperation(value = "根据id删除记录", notes = "此删除功能是逻辑删除")
    @ApiImplicitParam(name = "id",value = "数据id",required = true)
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Long id){
        return hospitalSetService.removeById(id) ? Result.ok().message("删除成功") : Result.fail().message("id不存在！");
    }

    /**
     * 批量删除
     * @param idList
     * @return
     */
    @ApiOperation(value = "根据id列表删除记录", notes = "此删除功能是逻辑删除")
    @DeleteMapping("delete")
    public Result batchRemove(@RequestBody List<Long> idList){
        return hospitalSetService.removeByIds(idList) ? Result.ok().message("删除成功") : Result.fail().message("删除失败");
    }

    //新增医院设置信息
    @ApiOperation(value = "新增医院设置信息",notes = "此功能用于新增医院设置信息")
    @PostMapping("/insert")
    public Result insert(@RequestBody HospitalSet hospitalSet){
        //设置状态1可用 0锁定
        try {
            hospitalSet.setStatus(1);
            //生成签名秘钥
            Random random = new Random();
            hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
            return hospitalSetService.save(hospitalSet) ? Result.ok().message("新增成功") : Result.fail().message("新增失败");
        } catch (DuplicateKeyException e) {
            throw new YyghException(ResultCodeEnum.HOSCODE_EXIST.getCode(), ResultCodeEnum.HOSCODE_EXIST.getMessage(), e);
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.FAIL.getCode(), ResultCodeEnum.FAIL.getMessage(), e);
        }
    }

    //修改医院设置信息
    @ApiOperation(value = "此功能用于修改医院设置信息")
    @PutMapping("/update")
    public Result update(@RequestBody HospitalSet hospitalSet){

        try {
            return hospitalSetService.updateById(hospitalSet) ? Result.ok().message("修改成功") : Result.fail().message("修改失败");
        } catch (DuplicateKeyException e) {
            throw new YyghException(ResultCodeEnum.HOSCODE_EXIST.getCode(), ResultCodeEnum.HOSCODE_EXIST.getMessage(), e);
        }catch (Exception e) {
            throw new YyghException(ResultCodeEnum.FAIL.getCode(), ResultCodeEnum.FAIL.getMessage(), e);
        }
    }

    //分页查询
    @ApiOperation(value = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",required = true),
            @ApiImplicitParam(name = "HospitalSetQueryVo",value = "查询对象")
    })
    @GetMapping("findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, HospitalSetQueryVo hospitalSetQueryVo){
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(hospitalSetQueryVo.getHosname()),"hosname", hospitalSetQueryVo.getHosname());
        queryWrapper.like(!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode()),"hoscode",hospitalSetQueryVo.getHoscode());
        hospitalSetService.pageMaps(page,queryWrapper);

//        page.getRecords().forEach((map)->{
//
//            map = null;
////            HashMap<String, Object> b = new HashMap<>();
////            map.forEach((k, v) -> b.put(StrUtil.toCamelCase(k), v)) ;
////
////            map = b;
//        });

//        List<Map<String, Object>> newList = new ArrayList<>();
//        for (int i = 0; i < page.getRecords().size(); i++) {
//            HashMap<String, Object> b = new HashMap<>();
//            Map<String, Object> map = page.getRecords().get(i);
//            map.forEach((k, v) -> b.put(StrUtil.toCamelCase(k), v)) ;
//            newList.add(b);
////            map = b;
//        }
//
//        page.setRecords(newList);
        //将page里面的record转换为驼峰
        List<Map<String, Object>> records = page.getRecords();
        records.forEach(map -> {
            Map<String, Object> updatedMap = new HashMap<>();
            map.forEach((k, v) -> updatedMap.put(StrUtil.toCamelCase(k), v));
            map.clear();
            map.putAll(updatedMap);
        });

        return Result.ok(page);
    }

    @ApiOperation(value = "医院设置锁定和解锁")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "医院设置id",required = true),
            @ApiImplicitParam(name = "status",value = "状态",required = true)
    })
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,@PathVariable Integer status){
        if (status != 0 && status != 1) {
            return Result.fail().message("非法参数");
        }
        HospitalSet hospitalSet = new HospitalSet();
        hospitalSet.setId(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok().message("状态修改成功");
    }



}

