package com.wangtao.social.square.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.square.api.dto.AddPostDTO;
import com.wangtao.social.square.api.dto.PostCommentDTO;
import com.wangtao.social.square.api.dto.PostQueryDTO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.api.vo.UserPostStatisticsVO;
import com.wangtao.social.square.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 添加帖子
     * @param postDTO 帖子内容
     * @return 帖子信息
     */
    @PostMapping("/add")
    public PostVO add(@Validated @RequestBody AddPostDTO postDTO) {
        return postService.addPost(postDTO);
    }

    /**
     * 删除帖子
     * @param id 帖子id
     */
    @GetMapping("/delete")
    public void delete(@RequestParam Long id) {
        postService.deletePost(id);
    }

    /**
     * 分页查询帖子列表(广场)
     * @param postQuery 查询参数
     * @return 帖子列表
     */
    @PostMapping("/list")
    public IPage<PostVO> list(@RequestBody PostQueryDTO postQuery) {
        return postService.list(postQuery);
    }

    /**
     * 分页查询自己的帖子列表
     * @param postQuery 查询参数
     * @return 帖子列表
     */
    @PostMapping("/listMyPost")
    public IPage<PostVO> listMyPost(@RequestBody PostQueryDTO postQuery) {
        return postService.listMyPost(postQuery);
    }

    /**
     * 给帖子添加评论
     * @param request 帖子评论
     * @return 评论信息
     */
    @PostMapping("/addComment")
    public CommentVO addComment(@Validated @RequestBody PostCommentDTO request) {
        return postService.addComment(request);
    }

    /**
     * 统计用户的发帖数量以及所有帖子的点赞数量
     * @return 用户的发帖数量以及所有帖子的点赞数量
     */
    @GetMapping("/userPostStatistics")
    public UserPostStatisticsVO userPostStatistics() {
        return postService.userPostStatistics();
    }
}
