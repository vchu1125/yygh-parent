package com.atguigu.yygh.oss.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Weizhu
 * @Date 2023/8/17 14:45
 * @注释
 */
public interface FileService {
    Map<String, String> upload(MultipartFile file) throws Exception;
}
