package com.wangtao.social.common.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wangtao.social.common.core.jackson.BigDecimalSerializer;
import com.wangtao.social.common.core.util.JavaTimeModuleUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

/**
 * @author wangtao
 * Created at 2021/6/4 18:29
 */
@Component
public class JacksonCustomizer implements Jackson2ObjectMapperBuilderCustomizer {

    public JacksonCustomizer() {
        super();
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = JavaTimeModuleUtils.create();

        SimpleModule simpleModule = new SimpleModule();
        // 添加BigDecimal的自定义序列化器
        simpleModule.addSerializer(new BigDecimalSerializer());

        /*
         * 1. java.util.Date yyyy-MM-dd HH:mm:ss
         * 2. 支持JDK8 LocalDateTime、LocalDate、 LocalTime
         * 3. Jdk8Module模块支持如Stream、Optional等类
         * 4. 序列化时包含所有字段
         * 5. 在序列化一个空对象时时不抛出异常
         * 6. 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
         * 7. BigDecimal.toPlainString()方法, 这样不会有科学计数法(序列化后仍是数字, 不是字符串)
         *    由于上面注册了自定义的BigDecimal序列化器, 该配置便没有效果了
         */
        builder.simpleDateFormat(JavaTimeModuleUtils.STANDARD_PATTERN)
                .timeZone(TimeZone.getDefault())
                .modules(javaTimeModule, new Jdk8Module(), simpleModule)
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    }
}
