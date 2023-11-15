package com.wangtao.social.gateway.autoconfig;

import com.wangtao.social.gateway.filter.CustomizeRateLimiterGatewayFilterFactory;
import com.wangtao.social.gateway.filter.IpKeyResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangtao
 * Created at 2023-11-11
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfig {

    @Bean
    public CustomizeRateLimiterGatewayFilterFactory defaultRateLimiterGatewayFilterFactory(RedisRateLimiter rateLimiter,
                                                                                           @Qualifier("ipKeyResolver") KeyResolver keyResolver) {
        return new CustomizeRateLimiterGatewayFilterFactory(rateLimiter, keyResolver);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return new IpKeyResolver();
    }
}
