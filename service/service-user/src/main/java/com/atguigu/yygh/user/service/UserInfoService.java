package com.atguigu.yygh.user.service;


import com.atguigu.yygh.model.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-15
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getByOpenId(String openid);
}
