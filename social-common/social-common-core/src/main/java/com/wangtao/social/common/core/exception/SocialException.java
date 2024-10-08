package com.wangtao.social.common.core.exception;

import java.io.Serial;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
public class SocialException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4431532607960940275L;

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
