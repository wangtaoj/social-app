package com.wangtao.social.common.core.response;

import java.lang.annotation.*;

/**
 * @author wangtao
 * Created at 2023-09-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface ServerReponseDecorator {

    /**
     * 是否需要包装结果
     * @return true/false
     */
    boolean ignore() default false;
}
