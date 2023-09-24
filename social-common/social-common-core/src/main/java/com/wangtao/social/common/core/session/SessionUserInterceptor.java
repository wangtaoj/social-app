package com.wangtao.social.common.core.session;

import com.wangtao.social.common.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * @author wangtao
 * Created at 2023-09-23
 */
@Component
public class SessionUserInterceptor implements HandlerInterceptor {

    private static final String SESSION_KEY = "social:session:";

    private final StringRedisTemplate redisTemplate;

    public SessionUserInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(token)) {
            SessionUser sessionUser = getSessionUser(token);
            if (Objects.nonNull(sessionUser)) {
                sessionUser.setToken(token);
                SessionUserHolder.set(sessionUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        SessionUserHolder.remove();
    }

    private SessionUser getSessionUser(String token) {
        String key = SESSION_KEY + token;
        String userJson = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(userJson)) {
            return null;
        }
        // 返回用户信息
        return JsonUtils.jsonToObj(userJson, SessionUser.class);
    }
}
