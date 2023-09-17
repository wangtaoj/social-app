package com.wangtao.social.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Data
public class RegisterRequestVO {

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
