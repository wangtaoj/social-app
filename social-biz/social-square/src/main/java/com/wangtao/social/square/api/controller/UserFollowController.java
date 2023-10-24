package com.wangtao.social.square.api.controller;

import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.square.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户关注
 * @author wangtao
 * Created at 2023-10-24
 */
@ServerReponseDecorator
@RequestMapping("/api/userfollow")
@RestController
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    /**
     * 关注
     *
     * @param userId 用户id
     * @param status 状态
     */
    @GetMapping("/follow")
    public void follow(@RequestParam("userId") Long userId, @RequestParam("status") Boolean status) {
        userFollowService.follow(userId, status);
    }
}
