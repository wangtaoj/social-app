package com.wangtao.social.user.controller;

import com.wangtao.social.common.core.response.ServerResponse;
import com.wangtao.social.user.service.AuthService;
import com.wangtao.social.user.vo.SmsCaptchaSendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 发送手机验证码
     *
     * @param smsCaptchaSend 请求参数
     * @return 空返回值
     */
    @PostMapping("/sendSmsCaptcha")
    public ServerResponse<Void> sendSmsCaptcha(@RequestBody SmsCaptchaSendVO smsCaptchaSend) {
        authService.sendSmsCaptcha(smsCaptchaSend);
        return ServerResponse.success();
    }
}
