package com.wangtao.social.common.core.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> {

    private final String code;

    private final String msg;

    private final T data;

    private ServerResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ServerResponse<Void> success() {
        return new ServerResponse<>(ResponseEnum.SUCCESS.getCode(), null, null);
    }

    public static ServerResponse<Void> successByMsg(String msg) {
        return new ServerResponse<>(ResponseEnum.SUCCESS.getCode(), msg, null);
    }

    public static <T> ServerResponse<T> success(T data) {
        return new ServerResponse<>(ResponseEnum.SUCCESS.getCode(), null, data);
    }

    public static <T> ServerResponse<T> error(ResponseEnum responseEnum) {
        return new ServerResponse<>(responseEnum.getCode(), responseEnum.getDesp(), null);
    }

    public static <T> ServerResponse<T> error(ResponseEnum responseEnum, String msg) {
        return new ServerResponse<>(responseEnum.getCode(), msg, null);
    }

    public static <T> ServerResponse<T> error(ResponseEnum responseEnum, String msg, T data) {
        return new ServerResponse<>(responseEnum.getCode(), msg, data);
    }

    public static ServerResponse<Object> error(BusinessException businessException) {
        return new ServerResponse<>(
                businessException.getResponseEnum().getCode(),
                businessException.getMessage(),
                businessException.getData()
        );
    }

    @JsonIgnore
    public boolean isSuccess() {
        return ResponseEnum.SUCCESS.getCode().equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
