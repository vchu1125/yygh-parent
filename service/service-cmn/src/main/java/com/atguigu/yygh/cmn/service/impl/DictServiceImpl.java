package com.atguigu.yygh.cmn.service.impl;


import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.mapper.DictTypeMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.cmn.DictType;
import com.atguigu.yygh.vo.cmn.DictTypeVo;
import com.atguigu.yygh.vo.cmn.DictVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Override
    public List<DictTypeVo> findAllDictList() {
        //获取字典类型列表
        List<DictType> dictTypes = dictTypeMapper.selectList(null);

        //获取字典数据列表
        List<Dict> dicts = dictMapper.selectList(null);

        List<DictTypeVo> DictTypeVoList = new ArrayList<>();

        //遍历字典类型列表
        dictTypes.forEach(dictType -> {
            DictTypeVo dictTypeVo = new DictTypeVo();
            //将DictType的id设置为dictTypeVo的id
            dictTypeVo.setId("parent-" + dictType.getId());
            dictTypeVo.setName(dictType.getName());
            //根据dictTypeId筛选dicts列表中的子节点
            List<Dict> childrenList = dicts.stream().filter(dict ->
                    //Long.longValue()对包装类进行拆箱，两个包装类比较结果是false
                    dictType.getId().longValue() == dict.getDictTypeId().longValue()
            ).collect(Collectors.toList());
            //遍历childrenList，将id，name，value，设置给dictVo
            List<DictVo> children = new ArrayList<>();
            childrenList.forEach(child -> {
                DictVo dictVo = new DictVo();
                dictVo.setId(""+child.getId());
                dictVo.setName(child.getName());
                dictVo.setValue(child.getValue());
                children.add(dictVo);
            });
            dictTypeVo.setChildren(children);
            //数据字典类型对象dictTypeVo添加到dictTypeVoList列表中
            DictTypeVoList.add(dictTypeVo);

        });
        return DictTypeVoList;
    }

    @Override
    public String getNameByDictTypeIdAndValue(Long dictTypeId, String value) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getDictTypeId,dictTypeId);
        wrapper.eq(Dict::getValue,value);
        Dict dict = dictMapper.selectOne(wrapper);
        if (dict == null){
            return "";
        }
        return dict.getName();
    }
}
