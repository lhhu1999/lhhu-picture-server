package com.lhhu.lhhupictureserver.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据转换类
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 9216010824284479178L;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;

}
