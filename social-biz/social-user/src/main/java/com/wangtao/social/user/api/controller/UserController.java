package com.wangtao.social.user.api.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.user.api.dto.UserDTO;
import com.wangtao.social.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 用户详情
     *
     * @return 用户详情
     */
    @GetMapping("/info")
    public UserDTO info() {
        return sysUserService.info();
    }

    @GetMapping("/infoById")
    public UserDTO infoById(@RequestParam("id") Long id){
        return sysUserService.infoById(id);
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     */
    @PostMapping("/updateInfo")
    public void updateInfo(@Validated @RequestBody UserDTO user) {
        sysUserService.updateInfo(user);
    }
}
