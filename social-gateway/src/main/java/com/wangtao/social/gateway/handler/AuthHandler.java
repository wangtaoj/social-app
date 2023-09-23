package com.wangtao.social.gateway.handler;


import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.redis.constant.AuthCacheConstant;
import com.wangtao.social.common.redis.util.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
@Slf4j
@Component
public class AuthHandler {

    private final StringRedisTemplate redisTemplate;

    public AuthHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void auth(String token) {
        String key = RedisKeyUtils.getSessionKey(token);
        String userJson = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(userJson)) {
            // 认证失败了，抛错
            throw new BusinessException(ResponseEnum.AUTH_FAIL, "您还未登录, 认证失败!");
        }

        // 自动续约
        redisTemplate.expire(key, AuthCacheConstant.SESSION_KEY_EXPIRED_TIME);
    }
}
