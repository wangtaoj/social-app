package com.wangtao.social.common.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wangtao.social.common.core.util.JavaTimeModuleUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

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

        /*
         * 1. java.util.Date yyyy-MM-dd HH:mm:ss
         * 2. 支持JDK8 LocalDateTime、LocalDate、 LocalTime
         * 3. Jdk8Module模块支持如Stream、Optional等类
         * 4. 序列化时包含所有字段
         * 5. 在序列化一个空对象时时不抛出异常
         * 6. 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
         * 7. 数字序列化成字符穿且调用BigDecimal.toPlainString()方法
         */
        builder.simpleDateFormat(JavaTimeModuleUtils.STANDARD_PATTERN)
                .modules(javaTimeModule, new Jdk8Module())
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .featuresToEnable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature());
    }
}
