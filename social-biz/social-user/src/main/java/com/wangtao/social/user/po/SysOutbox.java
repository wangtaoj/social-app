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
@TableName(value ="ss_sys_outbox")
public class SysOutbox extends BaseModel {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = -4297255465795835380L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 内容
     */
    private String content;
}