package com.wangtao.social.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
@Slf4j
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
        String ip = null;
        if (request.getRemoteAddress() != null) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        if (isLoopBackAddress(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error("获取本地ip地址发生异常", e);
            }
        }
        return ip;
    }

    private static boolean isLoopBackAddress(String address) {
        return Objects.equals("127.0.0.1", address) || Objects.equals("0:0:0:0:0:0:0:1", address);
    }
}
