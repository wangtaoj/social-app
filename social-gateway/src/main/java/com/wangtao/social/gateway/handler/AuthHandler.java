package com.wangtao.social.gateway.handler;


import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.util.JsonUtils;
import com.wangtao.social.common.redis.constant.AuthCacheConstant;
import com.wangtao.social.common.redis.util.RedisKeyUtils;
import com.wangtao.social.gateway.dto.SimpleUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public SimpleUserDTO auth(String token) {
        String key = RedisKeyUtils.getSessionKey(token);
        String userJson = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(userJson)) {
            // 认证失败了，抛错
            throw new BusinessException(ResponseEnum.AUTH_FAIL, "您还未登录, 认证失败!");
        }

        // 自动续约
        redisTemplate.expire(key, AuthCacheConstant.SESSION_KEY_EXPIRED_TIME);
        // 返回用户信息
        return JsonUtils.jsonToObj(userJson, SimpleUserDTO.class);
    }

    public SimpleUserDTO getUserInfo(String token) {
        if (StringUtils.isBlank(token)) {
            return new SimpleUserDTO();
        }
        String key = RedisKeyUtils.getSessionKey(token);
        String userJson = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(userJson)) {
            return new SimpleUserDTO();
        }

        // 自动续约
        redisTemplate.expire(key, AuthCacheConstant.SESSION_KEY_EXPIRED_TIME);
        // 返回用户信息
        return JsonUtils.jsonToObj(userJson, SimpleUserDTO.class);
    }
}
