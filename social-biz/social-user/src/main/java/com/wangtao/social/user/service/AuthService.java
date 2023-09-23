package com.wangtao.social.user.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.user.domain.SysUser;
import com.wangtao.social.user.dto.RegisterDTO;
import com.wangtao.social.user.dto.SmsCaptchaDTO;
import com.wangtao.social.user.enums.SmsCaptchaUseTypeEnum;
import com.wangtao.social.common.redis.constant.AuthCacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.wangtao.social.common.redis.util.RedisKeyUtils;

import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserService sysUserService;

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

    public void sendSmsCaptcha(SmsCaptchaDTO smsCaptcha) {
        if (SmsCaptchaUseTypeEnum.REGISTER.getType().equals(smsCaptcha.getSendSmsType())) {
            validSmsCaptchaForRegister(smsCaptcha);
        }
        String code = RandomUtil.randomNumbers(4);
        log.info("手机验证码: {}", code);
        String key = RedisKeyUtils.getSmsCaptchaKey(smsCaptcha.getPhone(), code);
        redisTemplate.opsForValue().set(key, code, AuthCacheConstant.SMS_CAPTCHA_KEY_EXPIRED_TIME);
    }

    private void validSmsCaptchaForRegister(SmsCaptchaDTO smsCaptchaSend) {
        // 检查手机号是否存在
        SysUser sysUser = sysUserService.selectByPhone(smsCaptchaSend.getPhone());
        if (Objects.nonNull(sysUser)) {
            throw new BusinessException(ResponseEnum.PHONE_REGISTERED);
        }

        // 验证图形验证码
        String captchaKey = RedisKeyUtils.getCaptchaKey(smsCaptchaSend.getCode());
        checkKey(captchaKey, "图形验证码不正确或失效!");
    }

    private void checkKey(String key, String errMsg) {
        Boolean isExist = redisTemplate.hasKey(key);
        if (isExist != null && isExist) {
            // 立即删除, 不等待key过期
            redisTemplate.delete(key);
        } else {
            throw new BusinessException(ResponseEnum.AUTH_FAIL, errMsg);
        }
    }

    public void register(RegisterDTO registerDTO) {
        // 验证短信验证码
        String smsCaptchaKey = RedisKeyUtils.getSmsCaptchaKey(registerDTO.getPhone(), registerDTO.getSmsCode());
        checkKey(smsCaptchaKey, "短信验证码不正确或失效!");

        // 检查手机号是否存在
        SysUser sysUserDb = sysUserService.selectByPhone(registerDTO.getPhone());
        if (Objects.nonNull(sysUserDb)) {
            throw new BusinessException(ResponseEnum.PHONE_REGISTERED);
        }

        // 保存用户
        SysUser sysUser = new SysUser();
        sysUser.setPhone(registerDTO.getPhone());
        sysUser.setPassword(MD5.create().digestHex(registerDTO.getPassword()));
        sysUserService.insert(sysUser);
    }
}
