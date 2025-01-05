package com.lhhu.lhhupictureserver.service;

import com.lhhu.lhhupictureserver.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Huaihu Li
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-01-05 14:33:47
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 加密工具类
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);
}
