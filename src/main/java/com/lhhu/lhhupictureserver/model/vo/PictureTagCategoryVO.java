package com.lhhu.lhhupictureserver.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PictureTagCategoryVO {
    // 标签列表
    private List<String> tagList;
    // 分类列表
    private List<String> categoryList;
}
