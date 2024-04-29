package com.wangtao.social.user.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wangtao.social.common.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@TableName(value ="ss_user_inbox")
@Setter
@Getter
public class UserInbox extends BaseModel {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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

    /**
     * 用户最新读取位置ID
     */
    private Long readPositionId;
}