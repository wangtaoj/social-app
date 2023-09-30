package com.wangtao.social.common.redis.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangtao
 * Created at 2023/7/2 16:36
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {

    String key();

    /**
     * 超时时间, 单位为秒
     */
    long timeout() default 30L;

    /**
     * 获取锁失败时是否抛异常
     */
    boolean throwExpWhenLockFail() default true;
}
