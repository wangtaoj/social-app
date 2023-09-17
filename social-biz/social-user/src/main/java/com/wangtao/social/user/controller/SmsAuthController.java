package com.wangtao.social.user.controller;

import com.wangtao.social.common.core.response.ServerResponse;
import com.wangtao.social.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
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
    public ServerResponse<String> getCaptcha() {
        return ServerResponse.success(authService.getCaptcha());
    }
}
