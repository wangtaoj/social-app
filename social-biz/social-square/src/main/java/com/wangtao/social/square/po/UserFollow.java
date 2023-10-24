package com.wangtao.social.square.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wangtao.social.common.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangtao
 * Created at 2023-10-24
 */
@Setter
@Getter
@TableName(value ="ss_user_follow")
public class UserFollow extends BaseModel {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关注的用户id
     */
    private Long followUserId;

    /**
     * 状态，false未关注，true关注
     */
    private Boolean status;
}