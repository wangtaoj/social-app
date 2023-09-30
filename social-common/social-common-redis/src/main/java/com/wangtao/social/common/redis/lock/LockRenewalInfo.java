package com.wangtao.social.common.redis.lock;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/7/2 18:56
 */
@Data
public class LockRenewalInfo {

    private String key;

    private long timeout;

    private TimeUnit unit;

    /**
     * 锁的开始时间(毫秒数)
     */
    private long base;

    private Thread thread;

    public LockRenewalInfo(String key, long timeout, TimeUnit unit, long base) {
        this.key = key;
        this.timeout = timeout;
        this.unit = unit;
        this.base = base;
    }

    /**
     * 最大续约次数
     */
    private int maxRenewalCount = 10;

    /**
     * 当前续约次数
     */
    private int curRenewalCount;

    /**
     * 达到超时时间的2/3
     */
    public boolean isReachRenewalTime() {
        long timeoutMills = unit.toMillis(timeout);
        long renewalMillis = base + timeoutMills * 2 / 3;
        return System.currentTimeMillis() > renewalMillis;
    }

    public boolean isReachMaxRenewalCount() {
        return curRenewalCount > maxRenewalCount;
    }

    public void updateRenewalInfo() {
        curRenewalCount++;
        // 锁重新生效时间
        base = System.currentTimeMillis();
    }
}
