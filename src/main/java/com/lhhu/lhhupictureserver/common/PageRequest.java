package com.lhhu.lhhupictureserver.common;

import lombok.Data;

/**
 * 通用分页请求
 */
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private int currentPage;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 排序字段
     */
    private String orderBy;
    /**
     * 排序顺序，默认升序
     */
    private String orderType = "desc";

}
