package com.wangtao.social.common.redis.lock;


import com.wangtao.social.common.redis.util.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/6/29 21:05
 */
@Component
public class RedisLock {

    private static final String LOCK_PREFIX = "lock:";

    private static final ThreadLocal<String> lockOwnerThreadLocal = new ThreadLocal<>();

    @Autowired
    private LockRenewalHandler lockRenewalHandler;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key key
     * @param timeout 超时时间
     * @param unit 超时时间单位
     * @return true/false
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        if (unit.compareTo(TimeUnit.SECONDS) < 0) {
            throw new IllegalArgumentException("时间单位必须大于等于秒");
        }
        final String lockOwner = UuidUtils.uuid();
        final String realKey = LOCK_PREFIX + key;
        Boolean res = redisTemplate.boundValueOps(realKey).setIfAbsent(lockOwner, timeout, unit);
        boolean success = res != null && res;
        if (success) {
            lockOwnerThreadLocal.set(lockOwner);
            lockRenewalHandler.addLockRenewalInfoAsync(realKey, timeout, unit, System.currentTimeMillis(), Thread.currentThread());
        }
        return success;
    }

    /**
     * 释放锁
     * 注: spring boot 2.1.x版本集群环境不支持lua脚本, 需要使用JedisCluster来操作
     * @param key 键
     * @return true / false
     */
    public boolean unlock(String key) {
        final String lockOwner = lockOwnerThreadLocal.get();
        if (StringUtils.isBlank(lockOwner)) {
            throw new IllegalMonitorStateException();
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setLocation(new ClassPathResource("lua/unlock.lua"));
        List<String> keys = Collections.singletonList(LOCK_PREFIX + key);
        List<String> params = Collections.singletonList(lockOwner);
        Long res = redisTemplate.execute(redisScript, keys, params.toArray());
        boolean success = res != null && res == 1L;
        if (success) {
            lockOwnerThreadLocal.remove();
        }
        return success;
    }
}
