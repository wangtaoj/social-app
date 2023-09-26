package com.wangtao.social.square.api.vo;

import com.wangtao.social.square.po.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@ToString
@Setter
@Getter
public class PostVO extends Post {

    private static final long serialVersionUID = 7601203071695848655L;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 用户点赞状态
     */
    private Boolean like;

    /**
     * 这是一个冗余字段(是否回复)，给前端用的，默认 false
     */
    private Boolean reply = Boolean.FALSE;

    /**
     * 这是一个冗余字段(回复内容)，给前端用的，
     */
    private String replyContent;
}
