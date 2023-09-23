package com.wangtao.social.gateway;

import com.wangtao.social.common.core.config.SocialAutoConfiguration;
import com.wangtao.social.gateway.autoconfig.GatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
@EnableConfigurationProperties({GatewayProperties.class})
@SpringBootApplication(
        exclude = {SocialAutoConfiguration.class},
        scanBasePackages = {
            "com.wangtao.social.gateway",
            "com.wangtao.social.common.redis"
        }
)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
