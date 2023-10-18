package com.wangtao.social.common.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author wangtao
 * Created at 2023-10-18
 */
@ToString
@Setter
@Getter
@Accessors(chain = true)
public class UserMessageDTO {

    /**
     * 消息数据唯一id
     */
    private String uuid;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 业务数据id
     */
    private Long itemId;

    /**
     * 内容
     */
    private String content;

    /**
     * 业务数据类型
     */
    private Integer serviceMessageType;

    /**
     * 发起方的用户ID
     */
    private Long fromUserId;

    /**
     * 接收方的用户ID
     */
    private Long toUserId;
}
