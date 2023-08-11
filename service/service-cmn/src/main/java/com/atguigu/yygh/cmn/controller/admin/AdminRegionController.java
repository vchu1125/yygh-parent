package com.atguigu.yygh.cmn.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.atguigu.yygh.cmn.service.RegionService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Region;
import com.atguigu.yygh.vo.cmn.RegionExcelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-08-01
 */
@Api(tags = "地区管理")
@RestController
@RequestMapping("admin/cmn/region")
@Slf4j
public class AdminRegionController {
    @Autowired
    private RegionService regionService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "根据上级code获取子节点数据列表")
    @ApiImplicitParam(name = "parentCode", value = "上级节点code", required = true)
    @GetMapping("/findRegionByParentCode/{parentCode}")
    public Result<List<Region>> findAllRegion(@PathVariable String parentCode){
        List<Region> regionList = regionService.findRegionByParentCode(parentCode);
        return Result.ok(regionList);
    }

/*
    @ApiOperation(value = "测试redis")
    @PostMapping("testRedisSet")
    public Result testRedisSet(){
        redisTemplate.opsForValue().set("username","wing");
        redisTemplate.opsForValue().set("region", new Region());
        return Result.ok();
    }
*/

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DownloadData}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("exportData")
    public void download(HttpServletResponse response) throws IOException {
        try {
            //int a = 9/0;
            //调用业务层查询数据
            List<RegionExcelVo> list = regionService.findRegionExcelVoList();
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), RegionExcelVo.class).sheet("模板").doWrite(list);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    @ApiOperation(value = "导入")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping("importData")
    public Result importData(MultipartFile file) throws IOException {
        regionService.importData(file);
        return Result.ok();
    }


}

