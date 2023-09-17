package com.wangtao.social.user.enums;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
public enum SmsCaptchaUseTypeEnum {

    /**
     * 注册
     */
    REGISTER("REGISTER"),

    /**
     * 登录
     */
    LOGIN("LOGIN"),

    ;

    private final String type;

    SmsCaptchaUseTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
