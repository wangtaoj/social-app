package com.wangtao.social.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
@ToString
@Setter
@Getter
public class UserDTO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 电话
     */
    private String phone;

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
