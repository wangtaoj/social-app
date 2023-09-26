package com.wangtao.social.square.api.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.square.api.dto.AddPostDTO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子
 * @author wangtao
 * Created at 2023-09-26
 */
@ServerReponseDecorator
@RequestMapping("/api/post")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 打声招呼吧
     * @return 招呼
     */
    @PostMapping("/add")
    public PostVO add(@Validated @RequestBody AddPostDTO postDTO) {
        return postService.addPost(postDTO);
    }
}
