package com.atguigu.yygh.cmn.controller.front;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.cmn.service.RegionService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.cmn.Region;
import com.atguigu.yygh.vo.cmn.RegionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/14 19:13
 * @注释
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/front/cmn/dict")
public class FrontDictController {

    @Resource
    private DictService dictService;
    @Resource
    private RegionService regionService;

    @ApiOperation("根据数据字典类型Id获取数据列表")
    @ApiImplicitParam(name = "dictTypeId",value = "类型id",required = true)
    @GetMapping("findDictList/{dictTypeId}")
    public Result<List<Dict>> findDictList(@PathVariable("dictTypeId") Long dictTypeId){
        List<Dict> dictList = dictService.findDictListByDictTypeId(dictTypeId);
        return Result.ok(dictList);
    }

    @ApiOperation("根据地区父Id获取数据列表")
    @ApiImplicitParam(name = "parentCode",value = "父id",required = true)
    @GetMapping("findRegionList/{parentCode}")
    public Result<List<RegionVo>> findRegionList(@PathVariable String parentCode){
        List<RegionVo> regionList = regionService.findRegionListByParentCode(parentCode);
        return Result.ok(regionList);
    }
}
