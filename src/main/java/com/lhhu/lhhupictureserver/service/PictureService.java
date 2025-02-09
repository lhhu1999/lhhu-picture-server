package com.lhhu.lhhupictureserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lhhu.lhhupictureserver.model.dto.picture.PictureQueryRequest;
import com.lhhu.lhhupictureserver.model.dto.picture.PictureReviewRequest;
import com.lhhu.lhhupictureserver.model.dto.picture.PictureUploadByBatchRequest;
import com.lhhu.lhhupictureserver.model.dto.picture.PictureUploadRequest;
import com.lhhu.lhhupictureserver.model.dto.user.UserQueryRequest;
import com.lhhu.lhhupictureserver.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lhhu.lhhupictureserver.model.entity.User;
import com.lhhu.lhhupictureserver.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author Huaihu Li
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-01-27 21:30:20
*/
public interface PictureService extends IService<Picture> {
    /**
     * 更新或者新增图片，上传云存储并写入数据库
     * @param inputSource 文件输入源
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取图片的封装对象
     * @param picture
     * @return
     */
    PictureVO getPictureVO(Picture picture);

    /**
     * 分页获取图片的封装对象
     * @param picturePage
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage);

    /**
     * 校验图片，更新和修改图片时使用
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 根据不同的图片查询条件生成不同的查询体
     * @param pictureQueryRequest
     * @return 对应查询体
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 图片审核
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核参数
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 批量抓取和创建图片
     * @param pictureUploadByBatchRequest
     * @param loginUser
     * @return 成功创建的图片数
     */
    Integer uploadPictureByBatch(PictureUploadByBatchRequest pictureUploadByBatchRequest, User loginUser);



}
