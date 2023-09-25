package com.wangtao.social.file.storage.api.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.file.storage.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片服务接口
 * @author wangtao
 * Created at 2023-09-25
 */
@ServerReponseDecorator
@RequestMapping("/api/image")
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传图片
     *
     * @param file 图片
     * @return 访问路径
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return imageService.upload(file);
    }
}
