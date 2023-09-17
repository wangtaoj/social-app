package constant;

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
}
