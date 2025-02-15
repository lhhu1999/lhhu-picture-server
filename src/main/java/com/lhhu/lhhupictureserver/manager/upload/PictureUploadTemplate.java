package com.lhhu.lhhupictureserver.manager.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.lhhu.lhhupictureserver.config.CosClientConfig;
import com.lhhu.lhhupictureserver.exception.BusinessException;
import com.lhhu.lhhupictureserver.exception.ErrorCode;
import com.lhhu.lhhupictureserver.manager.CosManager;
import com.lhhu.lhhupictureserver.model.dto.file.UploadPictureResult;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.OriginalInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosManager cosManager;

    /**
     * 上传图片
     * @param inputSource 文件或url
     * @param uploadPathPrefix 文件长传路径的前缀
     * @return
     */
    public UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1.校验图片
        validPicture(inputSource);
        // 2.图片上传地址
        String uuid = RandomUtil.randomString(8);
        String originalFilename = getOriginalFilename(inputSource);
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, "jpg");
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        
        File file = null;
        try {
            // 3.创建临时文件
            file = File.createTempFile(uploadPath, null);
            processFile(inputSource, file);
            // 4.上传图片, 返回上传结果
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            OriginalInfo originalInfo = putObjectResult.getCiUploadResult().getOriginalInfo();

            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                CIObject compressedCIObject = objectList.get(0);
                CIObject thumbnailCIObject = compressedCIObject;
                if (objectList.size() > 1) {
                    thumbnailCIObject = objectList.get(1);
                }
                return buildResult(originalFilename, compressedCIObject, thumbnailCIObject);
            }

            // 5.提取图片信息,返回封装结果
            ImageInfo imageInfo = originalInfo.getImageInfo();
            return buildResult(imageInfo, originalFilename, file, uploadPath);
        } catch (Exception e) {
            log.error("图像上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }finally {
            // 6.删除临时文件
            this.deleteTemFile(file);
        }
    }

    /**
     * 校验图片
     * @param inputSource
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取文件名
     * @param inputSource
     * @return
     */
    protected abstract String getOriginalFilename(Object inputSource);

    /**
     * 处理文件源，生成本地临时文件
     * @param inputSource
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 删除临时文件
     * @param file
     */
    public void deleteTemFile(File file) {
        if (file == null){
            return;
        }
        // 删除临时文件
        boolean deleteResult = file.delete();
        if (!deleteResult){
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }

    /**
     * 封装压缩后的返回结果
     *
     * @param originalFilename
     * @param compressedCIObject
     * @param thumbnailCIObject
     * @return
     */
    private UploadPictureResult buildResult(String originalFilename, CIObject compressedCIObject, CIObject thumbnailCIObject) {
        int picWidth = compressedCIObject.getWidth();
        int picHeight = compressedCIObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        // 返回封装后的结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCIObject.getFormat());
        uploadPictureResult.setPicSize(compressedCIObject.getSize().longValue());
        // 设置原图地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCIObject.getKey());
        // 设置缩略图地址
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailCIObject.getKey());
        return uploadPictureResult;
    }

    /**
     * 封装返回结果
     * @param imageInfo
     * @param originalFilename
     * @param file
     * @param uploadPath
     * @return
     */
    private UploadPictureResult buildResult(ImageInfo imageInfo, String originalFilename, File file, String uploadPath) {
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        // 返回封装后的结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(imageInfo.getFormat());
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        return uploadPictureResult;
    }

}
