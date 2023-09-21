package com.wangtao.social.common.core.aspect;

import com.wangtao.social.common.core.util.UuidUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author wangtao
 * Created at 2023-09-21
 */
@Aspect
@Component
public class RequestLogAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void before() {
        MDC.put("requestId", UuidUtils.uuid());
    }

    @After("pointCut()")
    public void after() {
        MDC.remove("requestId");
    }
}
