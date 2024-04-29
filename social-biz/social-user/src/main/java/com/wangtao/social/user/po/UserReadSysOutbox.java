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
 * Created at 2023-10-20
 */
@Setter
@Getter
@TableName(value ="ss_user_read_sys_outbox")
public class UserReadSysOutbox extends BaseModel {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = -7543928777740131252L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统收件箱数据读取id
     */
    private Long sysOutboxId;

    /**
     * 读取的用户id
     */
    private Long userId;

}