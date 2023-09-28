package com.wangtao.social.square.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wangtao.social.common.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 点赞信息
 * @author wangtao
 * Created at 2023-09-28
 */
@TableName(value ="ss_like")
@Setter
@Getter
public class Like extends BaseModel {

    @TableField(exist = false)
    private static final long serialVersionUID = 6603445188904700786L;

    /**
     * 注解id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 点赞条目id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long itemId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否点赞，true点赞，false未点赞
     */
    @TableField("`like`")
    private Boolean like;
}