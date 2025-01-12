package com.lhhu.lhhupictureserver.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据转换类
 * 用户注册请求
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 9216010824284479178L;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 确认密码
     */
    private String checkPassword;
}