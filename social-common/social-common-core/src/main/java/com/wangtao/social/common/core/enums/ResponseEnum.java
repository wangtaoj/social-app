package com.wangtao.social.common.core.enums;

import org.springframework.http.HttpStatus;

/**
 * 前3位数字代表业务, 后4位数字代表具体错误
 * @author wangtao
 * Created at 2023-09-16
 */
public enum ResponseEnum {

    SUCCESS("S200", "成功", HttpStatus.OK),

    SYS_ERROR("E0000001", "系统错误"),

    PARAM_ILLEGAL("E0000002", "参数不合法", HttpStatus.BAD_REQUEST),

    AUTH_FAIL("E0010001", "认证失败", HttpStatus.UNAUTHORIZED),

    PHONE_REGISTERED("E0010001", "手机号已注册"),
    ;

    private final String code;

    private final String desp;

    private final HttpStatus httpStatus;

    ResponseEnum(String code, String desp) {
        this(code, desp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    ResponseEnum(String code, String desp, HttpStatus httpStatus) {
        this.code = code;
        this.desp = desp;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDesp() {
        return desp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
