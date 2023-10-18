package com.wangtao.social.square.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@Data
public class CommentVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 条目id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long itemId;

    /**
     * 评论用户id
     */
    private Long userId;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 他的父级评论id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    /**
     * 被回复的评论id（没有则是回复父级评论，有则是回复这个人的评论）
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long replyId;

    private ReplyInfo replyInfo;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否为发布者
     */
    private Boolean publisher;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 当前登陆者是否点赞：true 已点赞
     */
    private Boolean like;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 该评论的所有孩子评论(分页)
     */
    private IPage<CommentVO> childCommentPage;


    /**
     * 这是一个冗余字段(是否回复)，给前端用的，默认 false
     */
    private Boolean reply = Boolean.FALSE;

    /**
     * 这是一个冗余字段(回复内容)，给前端用的，
     */
    private String replyContent;

    /**
     * 用于发送消息
     */
    private Long toUserId;

    @Data
    public static class ReplyInfo {
        /**
         * 评论用户id
         */
        private Long userId;

        /**
         * 被回复的内容
         */
        private String content;

        /**
         * 头像
         */
        private String avatarUrl;

        /**
         * 昵称
         */
        private String nickName;
    }
}
