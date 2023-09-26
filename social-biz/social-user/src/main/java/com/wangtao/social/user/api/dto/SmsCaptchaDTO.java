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
public class SmsCaptchaDTO {

    /**
     * 电话
     */
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    /**
     * 图形验证码
     */
    @NotBlank(message = "图形验证码不能为空")
    private String code;

    /**
     * 使用类型
     */
    @NotBlank(message = "使用类型不能为空")
    private String sendSmsType;
}
