package com.wangtao.social.file.storage.service;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.file.storage.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author wangtao
 * Created at 2023-09-25
 */
@Service
public class ImageService {

    @Autowired
    private MinioService minioService;

    public String upload(MultipartFile multipartFile) {
        String objName = minioService.createImageObjName(multipartFile.getOriginalFilename());
        try {
            minioService.uploadImage(objName, multipartFile.getInputStream(), multipartFile.getContentType());
            return minioService.getDirectAccessUrl(objName);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_FAIL);
        }
    }
}
