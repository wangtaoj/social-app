package com.wangtao.social.common.redis.config;

import com.wangtao.social.common.redis.util.RedisSerializerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author wangtao
 * Created at 2022/7/30 10:51
 */
@Configuration
public class RedisConfig {

    @Bean
    public <V> RedisTemplate<String, V> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, V> redisTemplate = new RedisTemplate<>();
        RedisSerializer<Object> valueRedisSerializer = RedisSerializerUtils.create();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(valueRedisSerializer);
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(valueRedisSerializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
