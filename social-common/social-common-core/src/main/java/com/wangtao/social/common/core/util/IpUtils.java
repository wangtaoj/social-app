package com.wangtao.social.common.core.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.Inet4Address;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
public final class IpUtils {

    private static final String X_REAL_IP = "X-Real-IP";

    private IpUtils() {}

    /**
     * 获取请求ip
     * @param request http请求
     * @return 请求ip
     */
    public static String getRemoteIp(ServerHttpRequest request) {
        String realIp = request.getHeaders().getFirst(X_REAL_IP);
        if (StringUtils.isNotBlank(realIp)) {
            return realIp;
        }
        if (request.getRemoteAddress() != null) {
            if (request.getRemoteAddress().getAddress() instanceof Inet4Address) {
                return request.getRemoteAddress().getHostString();
            }
        }
        return request.getURI().getHost();
    }
}
