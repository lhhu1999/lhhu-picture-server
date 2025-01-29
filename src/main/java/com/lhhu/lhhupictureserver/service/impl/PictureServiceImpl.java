package com.lhhu.lhhupictureserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhhu.lhhupictureserver.model.entity.Picture;
import com.lhhu.lhhupictureserver.service.PictureService;
import com.lhhu.lhhupictureserver.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author Huaihu Li
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-01-27 21:30:20
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}




