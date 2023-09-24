package com.wangtao.social.user.controller;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.user.dto.UserDTO;
import com.wangtao.social.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
@Slf4j
@ServerReponseDecorator
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登出
     */
    @GetMapping("/logout")
    public void logout() {
        sysUserService.logout();
    }

    @GetMapping("/info")
    public UserDTO info() {
        return sysUserService.info();
    }

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        log.info("{}", SessionUserHolder.getSessionUser());
        Map<String, Object> map = new HashMap<>();
        map.put("username", "wangtao");
        map.put("age", 20);
        map.put("amt", new BigDecimal("123456890.123000"));
        map.put("createTime", LocalDateTime.now());
        return map;
    }

    @GetMapping("/exp")
    public Map<String, Object> exp(String name) {
        System.out.println(name);
        throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "name is required!");
    }
}
