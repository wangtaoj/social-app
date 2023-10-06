package com.wangtao.social.square.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wangtao.social.common.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@ToString
@Setter
@Getter
@TableName(value ="ss_post_comment_parent")
public class PostCommentParent extends BaseModel {

    @TableField(exist = false)
    private static final long serialVersionUID = 3046432643612192693L;

    /**
     * 评论id
     */
    @TableId
    private Long id;

    /**
     * 条目id
     */
    private Long itemId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否为发布者
     */
    @TableField(value = "is_publisher")
    private Boolean publisher;

    /**
     * 删除标志
     */
    private Integer delFlg;

}