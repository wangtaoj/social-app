package com.wangtao.social.gateway.autoconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = "social.gateway")
public class GatewayProperties {

    /**
     * 局域网地址
     */
    public static final Set<String> LOCAL_AREA_IPS = Set.of("192.168", "127.0.0.1", "localhost");

    /**
     * 此url列表不用认证
     */
    private Set<String> exactIgnoreUrls = Collections.emptySet();

    /**
     * 黑名单列表
     */
    private Set<String> blackIds = Collections.emptySet();
}
