package com.wangtao.social.user.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Getter
@Setter
@ToString
public class RegisterDTO {

    /**
     * 电话
     */
    @NotBlank(message = "电话号码不为空")
    private String phone;

    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不为空")
    private String smsCode;

    /**
     * 密码
     */
    @NotBlank(message = "密码不为空")
    private String password;
}
