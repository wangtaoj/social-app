package com.wangtao.social.square.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wangtao.social.common.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 帖子
 * @author wangtao
 * Created at 2023-09-26
 */
@ToString
@Setter
@Getter
@TableName(value ="ss_post", autoResultMap = true)
public class Post extends BaseModel {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String[] imgUrlList;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 删除标志
     */
    private Integer delFlg;
}