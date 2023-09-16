package com.wangtao.social.common.core.exception;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
public class SocialException extends RuntimeException {

    public SocialException() {
        super();
    }

    public SocialException(String message) {
        super(message);
    }

    public SocialException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocialException(Throwable cause) {
        super(cause);
    }
}
