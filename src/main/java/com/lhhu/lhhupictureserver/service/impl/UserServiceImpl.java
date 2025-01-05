package com.lhhu.lhhupictureserver.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhhu.lhhupictureserver.exception.BusinessException;
import com.lhhu.lhhupictureserver.exception.ErrorCode;
import com.lhhu.lhhupictureserver.exception.ThrowUtils;
import com.lhhu.lhhupictureserver.model.entity.User;
import com.lhhu.lhhupictureserver.model.enums.UserRoleEnum;
import com.lhhu.lhhupictureserver.service.UserService;
import com.lhhu.lhhupictureserver.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author Huaihu Li
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-01-05 14:33:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "用户账号过短");
        if (userPassword.length() < 6 || checkPassword.length() < 6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }

        // 2. 检查账号是否和数据库中账号重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = this.baseMapper.selectCount(queryWrapper);
        ThrowUtils.throwIf(count>0, ErrorCode.PARAMS_ERROR, "账号已存在");

        // 3. 密码加密
        String encryptPassword = getEncryptPassword(userAccount);

        // 4. 插入数据到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        return user.getId();
    }

    /**
     * 加密密码
     * @param userPassword
     * @return
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 加盐， 混淆密码
        final String SALT = "lhhu";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}




