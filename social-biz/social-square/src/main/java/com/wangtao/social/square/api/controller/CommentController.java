package com.wangtao.social.square.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.square.api.dto.PostCommentQueryDTO;
import com.wangtao.social.square.api.vo.CommentListVO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@ServerReponseDecorator
@RequestMapping("/api/comment")
@RestController
public class CommentController {

    @Autowired
    private PostCommentService postCommentService;

    /**
     * 分页查询帖子一级评论
     * @param commentQueryDTO 查询参数
     * @return 一级评论信息
     */
    @PostMapping("/listOneCommentOfPost")
    public CommentListVO listOneCommentOfPost(@Validated @RequestBody PostCommentQueryDTO commentQueryDTO) {
        return postCommentService.listOneComment(commentQueryDTO);
    }

    /**
     * 分页查询帖子二级评论
     * @param commentQueryDTO 查询参数
     * @return 二级评论信息
     */
    @PostMapping("/listTwoCommentOfPost")
    public IPage<CommentVO> listTwoCommentOfPost(@Validated @RequestBody PostCommentQueryDTO commentQueryDTO) {
        return postCommentService.listTwoComment(commentQueryDTO, true);
    }
}
