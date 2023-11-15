package com.wangtao.social.gateway.filter;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.response.ServerResponse;
import com.wangtao.social.common.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义返回值
 * 注意: 不能使用快捷方式配置
 * @author wangtao
 * Created at 2023-11-15
 */
@Slf4j
public class CustomizeRateLimiterGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory {

    public CustomizeRateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);
    }

    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = getOrDefault(config.getKeyResolver(), getDefaultKeyResolver());
        @SuppressWarnings("unchecked")
        RateLimiter<Object> limiter = getOrDefault(config.getRateLimiter(), getDefaultRateLimiter());
        return (exchange, chain) -> resolver.resolve(exchange).flatMap(key -> {
            String routeId = config.getRouteId();
            if (routeId == null) {
                Route route = Objects.requireNonNull(exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR));
                routeId = route.getId();
            }
            return limiter.isAllowed(routeId, key).flatMap(response -> {
                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                }
                if (response.isAllowed()) {
                    return chain.filter(exchange);
                }
                log.info("the request is limited, url: {}, clientIp: {}", exchange.getRequest().getURI().getPath(), key);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                exchange.getResponse().setStatusCode(config.getStatusCode());
                ServerResponse<?> serverResponse = ServerResponse.error(ResponseEnum.SYS_LIMIT_FLOW);
                String returnStr = JsonUtils.objToJson(serverResponse);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Flux.just(buffer));
            });
        });
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }
}
