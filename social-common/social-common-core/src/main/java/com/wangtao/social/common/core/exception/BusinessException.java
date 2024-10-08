package com.wangtao.social.common.core.exception;

import com.wangtao.social.common.core.enums.ResponseEnum;

import java.io.Serial;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
public class BusinessException extends SocialException {

    @Serial
    private static final long serialVersionUID = 236410471132246058L;

    private final ResponseEnum responseEnum;

    private Object data;

    public BusinessException(ResponseEnum responseEnum) {
        this(responseEnum, (Object) null);
    }

    public BusinessException(ResponseEnum responseEnum, Object data) {
        super(responseEnum.getDesp());
        this.responseEnum = responseEnum;
        this.data = data;
    }

    public BusinessException(ResponseEnum responseEnum, String message) {
        this(responseEnum, message, null);
    }

    public BusinessException(ResponseEnum responseEnum, String message, Object data) {
        super(message);
        this.responseEnum = responseEnum;
        this.data = data;
    }

    public BusinessException(ResponseEnum responseEnum, String message, Throwable cause) {
        super(message, cause);
        this.responseEnum = responseEnum;
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public Object getData() {
        return data;
    }
}
