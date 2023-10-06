package com.wangtao.social.square.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@ToString
@Setter
@Getter
public class PostCommentDTO {
    /**
     * 帖子id
     */
    @NotNull(message = "帖子id不为空")
    private Long postId;

    /**
     * 回复id，如：a 回复 b 的评论，那么回复id 就是 b 的评论id
     */
    private Long replyId;

    /**
     * 父级评论id
     */
    private Long parentId;

    /**
     * 内容
     */
    @NotNull(message = "评论内容不为空")
    private String content;
}
