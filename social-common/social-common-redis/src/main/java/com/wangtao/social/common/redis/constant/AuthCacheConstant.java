package com.wangtao.social.common.redis.constant;

import java.time.Duration;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
public interface AuthCacheConstant {

    /**
     * 图形验证码key
     */
    String CAPTCHA_KEY = GlobalCacheConstant.SYS_GLOBAL_KEY + "captcha:";

    /**
     * 图形验证码超时时间
     */
    Duration CAPTCHA_KEY_EXPIRED_TIME = Duration.ofMinutes(10);

    /**
     * 手机验证码
     */
    String SMS_CAPTCHA_KEY = GlobalCacheConstant.SYS_GLOBAL_KEY + "sms:captcha:";

    /**
     * 手机验证码超时时间
     */
    Duration SMS_CAPTCHA_KEY_EXPIRED_TIME = Duration.ofMinutes(10);

    /**
     * session key
     */
    String SESSION_KEY = GlobalCacheConstant.SYS_GLOBAL_KEY + "session:";

    /**
     * session过期时间
     */
    Duration SESSION_KEY_EXPIRED_TIME = Duration.ofMinutes(30);
}
