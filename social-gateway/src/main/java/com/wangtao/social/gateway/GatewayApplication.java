package com.wangtao.social.gateway;

import com.wangtao.social.common.core.config.SocialAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
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
