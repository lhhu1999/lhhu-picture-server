package com.lhhu.lhhupictureserver.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求
 */
@Data
public class DeleteRequest implements Serializable {
    private int id;

    private static final long serialVersionUID = 1L;

}
