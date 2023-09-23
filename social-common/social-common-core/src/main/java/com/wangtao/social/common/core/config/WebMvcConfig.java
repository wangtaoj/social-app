package com.wangtao.social.common.core.config;

import com.wangtao.social.common.core.session.SessionUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
@Configuration(proxyBeanMethods = false)
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionUserInterceptor sessionUserInterceptor;

    public WebMvcConfig(SessionUserInterceptor sessionUserInterceptor) {
        this.sessionUserInterceptor = sessionUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionUserInterceptor)
                .addPathPatterns("/**");
    }
}
