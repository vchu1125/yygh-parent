package com.atguigu.yygh.cmn.controller.admin;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictTypeVo;
import com.atguigu.yygh.vo.cmn.DictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("admin/cmn/dict")
public class AdminDictController {
    @Autowired
    private DictService dictService;

    @ApiOperation("查询所有的数据字典")
    @GetMapping("/findAll")
    public Result<List<DictTypeVo>> findAll(){

        List<DictTypeVo> list = dictService.findAllDictList();
        return Result.ok(list);
    }

}

