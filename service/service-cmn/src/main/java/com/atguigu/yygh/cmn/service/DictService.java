package com.atguigu.yygh.cmn.service;


import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictTypeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
public interface DictService extends IService<Dict> {

    List<DictTypeVo> findAllDictList();

    /**
     * 根据dictTypeId和value查询查询dictName
     * @param dictTypeId
     * @param value
     * @return
     */
    String getNameByDictTypeIdAndValue(Long dictTypeId, String value);
}
