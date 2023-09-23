package com.wangtao.social.common.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangtao
 * Created at 2023-09-10
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfig {

    @ConditionalOnMissingBean
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.info.desp}")String desp) {
        return new OpenAPI()
                .info(new Info()
                        .title("Social APP API")
                        .version("1.0")
                        .description(desp)
                        .termsOfService("wangtaoj.github.io")
                        .license(new License().name("Apache 2.0").url("wangtaoj.github.io"))
                );
    }
}
