package com.wangtao.social.common.redis.lock.aspect;


import com.wangtao.social.common.redis.lock.LockFailException;
import com.wangtao.social.common.redis.lock.RedisLock;
import com.wangtao.social.common.redis.lock.annotation.DistributeLock;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/7/2 16:38
 */
@Aspect
@Component
public class DistributeLockAspect {

    @Autowired
    private RedisLock redisLock;

    @Around("@annotation(distributeLock)")
    public Object doAroud(ProceedingJoinPoint jp, DistributeLock distributeLock) throws Throwable {
        final String key = distributeLock.key();
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("分布式锁键不能为空");
        }
        boolean isLock = redisLock.tryLock(key, distributeLock.timeout(), TimeUnit.SECONDS);
        if (isLock) {
            try {
                return jp.proceed();
            } finally {
                redisLock.unlock(key);
            }
        } else {
            if (distributeLock.throwExpWhenLockFail()) {
                throw new LockFailException("lock fail");
            }
            // 获取不到锁, 不抛异常, 返回默认结果
            return null;
        }
    }
}
