package com.lhhu.lhhupictureserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lhhu.lhhupictureserver.model.dto.user.UserQueryRequest;
import com.lhhu.lhhupictureserver.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhhu.lhhupictureserver.model.vo.LoginUserVO;
import com.lhhu.lhhupictureserver.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 属性复制
     * @param user
     * @return 脱敏后用户登录信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 属性复制
     * @param user
     * @return 脱敏后用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 属性复制
     * @param userList
     * @return 脱敏后用户信息列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    boolean userLoginOut(HttpServletRequest request);

    /**
     * 根据不同的用户查询条件生成不同的查询体
     * @param userQueryRequest
     * @return 对应查询体
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

}
