package com.wangtao.social.common.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
@ComponentScan(basePackages = {"com.wangtao.social"})
@Configuration(proxyBeanMethods = false)
public class SocialAutoConfiguration {
}
