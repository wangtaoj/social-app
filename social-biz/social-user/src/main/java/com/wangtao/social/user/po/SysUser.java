package com.wangtao.social.user.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wangtao.social.common.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Setter
@Getter
@TableName(value ="sys_user")
public class SysUser extends BaseModel implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 344979719689063146L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 电话
     */
    private String phone;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 
     */
    private String openid;

    /**
     * 
     */
    private String sessionKey;

    /**
     * 
     */
    private String unionid;

    /**
     * 性别，0女1男
     */
    private Integer sex;

    /**
     * 简介
     */
    private String intro;

}