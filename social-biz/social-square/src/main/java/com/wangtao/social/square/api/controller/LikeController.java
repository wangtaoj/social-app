package com.wangtao.social.square.api.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.square.api.dto.LikeDTO;
import com.wangtao.social.square.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 * Created at 2023-09-28
 */
@ServerReponseDecorator
@RequestMapping("/api/like")
@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/likeByDb")
    public void likeByDb(@Validated @RequestBody LikeDTO likeDTO) {
        likeService.likeByDb(likeDTO);
    }
}
