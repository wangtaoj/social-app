package com.wangtao.social.user.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.user.dto.RegisterDTO;
import com.wangtao.social.user.dto.SmsCaptchaDTO;
import com.wangtao.social.user.service.AuthService;
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
}
