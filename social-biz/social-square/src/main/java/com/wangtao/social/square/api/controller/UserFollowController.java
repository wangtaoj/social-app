package com.wangtao.social.square.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.square.api.dto.UserFollowDTO;
import com.wangtao.social.square.api.vo.UserFollowVO;
import com.wangtao.social.square.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户关注
 * @author wangtao
 * Created at 2023-10-24
 */
@ServerReponseDecorator
@RequestMapping("/api/userFollow")
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

    /**
     * 查询用户的关注列表
     *
     * @param request 参数
     * @return 关注列表
     */
    @PostMapping("/followUserPage")
    public IPage<UserFollowVO> followUserPage(@Validated @RequestBody UserFollowDTO request) {
        return userFollowService.listFollow(request);
    }

    /**
     * 查询用户的粉丝列表
     *
     * @param request 参数
     * @return 粉丝列表
     */
    @PostMapping("/followerUserPage")
    public IPage<UserFollowVO> followerUserPage(@Validated @RequestBody UserFollowDTO request) {
        return userFollowService.listFollower(request);
    }
}
