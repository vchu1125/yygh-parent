package com.atguigu.yygh.vo.cmn;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author Weizhu
 * @Date 2023/8/14 19:51
 * @注释
 */
@Data
@ApiModel(description = "地区名称Vo")
public class RegionVo {

    private String name;

    private String code;


}
