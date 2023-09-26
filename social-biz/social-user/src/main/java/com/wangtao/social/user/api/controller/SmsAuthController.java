package com.wangtao.social.user.api.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.user.api.dto.LoginDTO;
import com.wangtao.social.user.api.dto.RegisterDTO;
import com.wangtao.social.user.api.dto.SmsCaptchaDTO;
import com.wangtao.social.user.service.AuthService;
import com.wangtao.social.user.validator.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@ServerReponseDecorator
@RequestMapping("/sms/auth")
@RestController
public class SmsAuthController {

    @Autowired
    private AuthService authService;

    /**
     * 获取图片验证码
     *
     * @return 图片验证码
     */
    @GetMapping("/getCaptcha")
    public String getCaptcha() {
        return authService.getCaptcha();
    }

    /**
     * 发送手机验证码
     *
     * @param smsCaptcha 请求参数
     */
    @PostMapping("/sendSmsCaptcha")
    public void sendSmsCaptcha(@Validated @RequestBody SmsCaptchaDTO smsCaptcha) {
        authService.sendSmsCaptcha(smsCaptcha);
    }

    /**
     * 用户注册
     *
     * @param registerDTO 请求参数
     */
    @PostMapping("/register")
    public void register(@Validated @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
    }

    /**
     * 根据手机号码+短信验证码登录
     *
     * @param loginDTO 登录参数
     * @return 登录token
     */
    @PostMapping("/loginBySmsCode")
    public String loginBySmsCode(@Validated({AuthValidator.BySmsCode.class}) @RequestBody LoginDTO loginDTO) {
        return authService.loginBySmsCode(loginDTO);
    }

    /**
     * 根据手机号码+密码登录
     *
     * @param loginDTO 登录参数
     * @return 登录token
     */
    @PostMapping("/loginByPassword")
    public String loginByPassword(@Validated({AuthValidator.ByPassword.class}) @RequestBody LoginDTO loginDTO) {
        return authService.loginByPassword(loginDTO);
    }
}
