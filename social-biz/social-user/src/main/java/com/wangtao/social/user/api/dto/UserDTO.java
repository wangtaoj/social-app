package com.wangtao.social.user.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "电话号码不能为空")
    private String phone;

    /**
     * 密码
     * 序列化会忽略该字段，反序列化不会忽略
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 性别，0女1男
     */
    private Integer sex;

    /**
     * 简介
     */
    private String intro;
}
