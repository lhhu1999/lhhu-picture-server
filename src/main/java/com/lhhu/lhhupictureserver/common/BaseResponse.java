package com.lhhu.lhhupictureserver.common;

import com.lhhu.lhhupictureserver.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局响应封装类，支持序列化
 * @param <T>
 */

@Data
public class BaseResponse <T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private String message;


    public BaseResponse(int code, T data, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
