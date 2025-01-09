package com.lhhu.lhhupictureserver.service;

import com.lhhu.lhhupictureserver.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhhu.lhhupictureserver.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

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
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param request
     * @return 脱敏后用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 加密工具类
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);

    /**
     * 属性复制
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    boolean userLoginOut(HttpServletRequest request);
}
