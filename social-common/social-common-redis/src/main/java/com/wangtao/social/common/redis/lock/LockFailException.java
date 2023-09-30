package com.wangtao.social.common.redis.lock;

/**
 * @author wangtao
 * Created at 2023/7/2 16:49
 */
public class LockFailException extends RuntimeException {

    private static final long serialVersionUID = 6060771366348159321L;

    public LockFailException() {
    }

    public LockFailException(String message) {
        super(message);
    }

    public LockFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
