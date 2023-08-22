package com.atguigu.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.oss.service.FileService;
import com.atguigu.yygh.oss.utils.ConstantProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Weizhu
 * @Date 2023/8/17 14:45
 * @注释
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Resource
    private ConstantProperties constantProperties;

    @Override
    public Map<String, String> upload(MultipartFile file) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        //String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        String endpoint = constantProperties.getEndpoint();
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = constantProperties.getBucketName();

        //文件名称
        String originalFilename = file.getOriginalFilename();
        String dateStr = new DateTime().toString("yyyyMMdd");
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName =
                dateStr
                        + "/" + UUID.randomUUID().toString().replace("-", "")
                        + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);


        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            //设置该属性可以返回response
            putObjectRequest.setProcess("true");
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            //如果上传成功，则返回200
            log.info(Integer.toString(result.getResponse().getStatusCode()));
            if (result.getResponse().getStatusCode() != 200) {
                throw new YyghException(ResultCodeEnum.FAIL);
            }

            // 设置签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);

            HashMap<String, String> map = new HashMap<>();
            map.put("previewUrl", url.toString());
            map.put("url", objectName);

            return map;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
            return null;
    }

    @Override
    public String getPreviewUrl(String objectName) {
        OSS ossClient = null;
        try {
            // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
            String endpoint = constantProperties.getEndpoint();
            // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            // 填写Bucket名称，例如examplebucket。
            String bucketName = constantProperties.getBucketName();
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
            // 设置签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
            System.out.println("url = " + url.toString());
            return url.toString();
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.FAIL.getCode(), "失败",e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
