package com.wangtao.social.common.core.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangtao
 * Created at 2023-10-19
 */
@Slf4j
public class DefaultThreadFactory implements ThreadFactory {

    private static final Thread.UncaughtExceptionHandler DEFAULT_HANDLER = (t, e) -> log.error(e.getMessage(), e);

    private final String prefix;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = DEFAULT_HANDLER;

    public DefaultThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public DefaultThreadFactory(String prefix, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.prefix = prefix;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread t = new Thread(r, prefix + "-thread-" + threadNumber.getAndIncrement());
        // 捕获异常, 默认只打印到控制台, 参见Thread.dispatchUncaughtException
        t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        return t;
    }
}
