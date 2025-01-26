package com.lhhu.lhhupictureserver.controller;

import com.lhhu.lhhupictureserver.annotation.AuthCheck;
import com.lhhu.lhhupictureserver.common.BaseResponse;
import com.lhhu.lhhupictureserver.common.ResultUtils;
import com.lhhu.lhhupictureserver.constant.UserConstant;
import com.lhhu.lhhupictureserver.exception.BusinessException;
import com.lhhu.lhhupictureserver.exception.ErrorCode;
import com.lhhu.lhhupictureserver.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManager cosManager;

    /**
     * 测试文件上传
     * @param multipartFile
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        // 本地临时文件位置，以及要上传到服务器的哪个位置
        String filepath = String.format("/test/%s", filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            return ResultUtils.success(filepath);
        } catch (Exception e) {
            log.error("File upload error, filepath=" + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }finally {
            if (file != null) {
                // 删除本地临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("File delete error, filepath={}", filepath);
                }
            }
        }
    }
}
