package com.wangtao.social.gateway.filter;

import com.wangtao.social.common.core.constant.UrlConstant;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.response.ServerResponse;
import com.wangtao.social.common.core.util.IpUtils;
import com.wangtao.social.common.core.util.JsonUtils;
import com.wangtao.social.gateway.autoconfig.GatewayProperties;
import com.wangtao.social.gateway.handler.AuthHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter {

    private final AuthHandler authHandler;

    private final GatewayProperties gatewayProperties;

    public AuthGlobalFilter(AuthHandler authHandler, GatewayProperties gatewayProperties) {
        this.authHandler = authHandler;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 黑名单校验
        if (forbidBlackIp(exchange.getRequest())) {
            return sendError(exchange.getResponse(), new BusinessException(ResponseEnum.NO_PERMISSION));
        }

        // 无须认证的url
        String requestUrl = exchange.getRequest().getURI().getPath();
        if (ignoreExactUrl(requestUrl)) {
            return chain.filter(exchange);
        }

        // feign 请求检查
        if (requestUrl.contains(UrlConstant.FEGIN_PREFIX)) {
            return sendError(exchange.getResponse(), new BusinessException(ResponseEnum.NO_PERMISSION));
        }

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        ServerHttpResponse resp = exchange.getResponse();
        try {
            authHandler.auth(token);
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("认证失败, 请求url: {}", requestUrl, e);
            return sendError(resp, e);
        }
    }

    private boolean ignoreExactUrl(String requestUrl) {
        Set<String> exactIgnoreUrls = gatewayProperties.getExactIgnoreUrls();
        for (String ignoreUrl : exactIgnoreUrls) {
            if (requestUrl.endsWith(ignoreUrl)) {
                return true;
            }
        }
        return false;
    }

    private boolean forbidBlackIp(ServerHttpRequest request) {
        String requestIp = IpUtils.getRemoteIp(request);
        for (String localAreaIp : GatewayProperties.LOCAL_AREA_IPS) {
            if (requestIp.startsWith(localAreaIp)) {
                return false;
            }
        }
        Set<String> blackIps = gatewayProperties.getBlackIds();
        return blackIps.contains(requestIp);
    }

    private Mono<Void> sendError(ServerHttpResponse resp, Exception e) {
        ServerResponse<?> serverResponse;
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            serverResponse = ServerResponse.error(businessException);
            // 设置响应 code
            resp.setStatusCode(businessException.getResponseEnum().getHttpStatus());
        } else {
            serverResponse = ServerResponse.error(ResponseEnum.SYS_ERROR);
            resp.setStatusCode(ResponseEnum.SYS_ERROR.getHttpStatus());
        }
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String returnStr = JsonUtils.objToJson(serverResponse);
        assert returnStr != null;
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

}
