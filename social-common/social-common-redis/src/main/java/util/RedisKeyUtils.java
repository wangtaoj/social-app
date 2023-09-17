package util;

import constant.AuthCacheConstant;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
public final class RedisKeyUtils {

    private RedisKeyUtils() {
    }

    /**
     * 获取图形验证码key
     *
     * @param code 验证码内容
     * @return 图形验证码key
     */
    public static String getCaptchaKey(String code) {
        return AuthCacheConstant.CAPTCHA_KEY + code;
    }

    /**
     * 获取手机验证码key
     *
     * @param phone 手机
     * @param code 验证码内容
     * @return 图形验证码key
     */
    public static String getSmsCaptchaKey(String phone, String code) {
        return AuthCacheConstant.SMS_CAPTCHA_KEY + phone + ":" + code;
    }
}
