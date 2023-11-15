package com.wangtao.social.gateway.filter;

import com.wangtao.social.common.core.util.IpUtils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用于限流的key, 使用ip
 * @author wangtao
 * Created at 2023-11-11
 */
public class IpKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // 这里使用 IP 做 key
        return Mono.just(IpUtils.getRemoteIp(exchange.getRequest()));
    }
}
