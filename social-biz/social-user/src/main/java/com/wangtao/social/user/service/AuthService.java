package com.wangtao.social.user.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import constant.AuthCacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import util.RedisKeyUtils;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getCaptcha() {
        // 生成图形验证码
        RandomGenerator randomGenerator = new RandomGenerator("abcdefghjkmnopqrstuvwxyz0123456789", 4);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 42, 4, 60);
        lineCaptcha.setGenerator(randomGenerator);
        lineCaptcha.createCode();

        // 获取 base64 字符串 和 code 值
        String base64 = "data:image/png;base64," + lineCaptcha.getImageBase64();
        String code = lineCaptcha.getCode();

        log.info("图形验证码内容：{}", code);
        redisTemplate.opsForValue().set(RedisKeyUtils.getCaptchaKey(code), code, AuthCacheConstant.CAPTCHA_KEY_EXPIRED_TIME);
        return base64;
    }
}
