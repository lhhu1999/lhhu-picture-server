package com.lhhu.lhhupictureserver.controller;

import com.lhhu.lhhupictureserver.common.BaseResponse;
import com.lhhu.lhhupictureserver.common.ResultUtils;
import com.lhhu.lhhupictureserver.exception.ErrorCode;
import com.lhhu.lhhupictureserver.exception.ThrowUtils;
import com.lhhu.lhhupictureserver.model.dto.UserRegisterRequest;
import com.lhhu.lhhupictureserver.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    
    /**
     * 用户注册
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }
}
