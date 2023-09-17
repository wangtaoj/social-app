package com.wangtao.social.user.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Getter
@Setter
public class SmsCaptchaSendVO {

    /**
     * 电话
     */
    private String phone;

    /**
     * 图形验证码
     */
    private String code;

    /**
     * 使用类型
     */
    private String sendSmsType;
}
