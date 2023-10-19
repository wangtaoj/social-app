package com.wangtao.social.square.config;

import com.wangtao.social.common.core.executor.DefaultThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023-10-19
 */
@Configuration(proxyBeanMethods = false)
public class ExecutorConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService mqSenderExecutor() {
        return new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new DefaultThreadFactory("mqSender"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
