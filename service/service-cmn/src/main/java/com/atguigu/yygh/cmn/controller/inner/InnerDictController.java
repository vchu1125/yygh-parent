package com.atguigu.yygh.cmn.controller.inner;

import com.atguigu.yygh.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Weizhu
 * @Date 2023/8/9 20:44
 * @注释
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("/inner/cmn/dict")
public class InnerDictController {
    @Resource
    private DictService dictService;

    @ApiOperation("获取数据字典名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeId",value = "字典类型id",required = true),
            @ApiImplicitParam(name = "value",value = "数据字典值",required = true)
    })
    @GetMapping("/getName/{dictTypeId}/{value}")
    public String getName(@PathVariable("dictTypeId") Long dictTypeId,
                          @PathVariable("value") String value){
        String dictName = dictService.getNameByDictTypeIdAndValue(dictTypeId,value);
//        try {
//            //模拟网络延迟
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return dictName;
    }
}
