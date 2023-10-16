package com.wangtao.social.user.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wangtao.social.common.user.enums.UserCenterServiceMessageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@ToString
@Setter
@Getter
public class UserMessageVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 发送用户id
     */
    private Long fromUserId;

    /**
     * 接收用户id
     */
    private Long toUserId;

    /**
     * 发送方头像
     */
    private String avatarUrl;

    /**
     * 发送方昵称
     */
    private String nickName;

    /**
     * 帖子id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long postId;

    /**
     * 业务数据id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long itemId;

    /**
     * 业务数据类型
     */
    private UserCenterServiceMessageTypeEnum serviceMessageType;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 用户最新读取位置ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long readPositionId;

    /**
     * 消息创建时间
     */
    private LocalDateTime createTime;
}
