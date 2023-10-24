package com.wangtao.social.square.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.wangtao.social.api.user.feign.UserFeignClient;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.common.user.dto.UserMessageDTO;
import com.wangtao.social.common.user.enums.UserCenterMessageTypeEnum;
import com.wangtao.social.common.user.enums.UserCenterServiceMessageTypeEnum;
import com.wangtao.social.square.api.dto.UserFollowDTO;
import com.wangtao.social.square.api.vo.UserFollowVO;
import com.wangtao.social.square.mapper.UserFollowMapper;
import com.wangtao.social.square.mapper.UserFollowerMapper;
import com.wangtao.social.square.mq.FollowEvent;
import com.wangtao.social.square.po.UserFollow;
import com.wangtao.social.square.po.UserFollower;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangtao
 * Created at 2023-10-24
 */
@Service
public class UserFollowService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private UserFollowerMapper userFollowerMapper;

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Transactional(rollbackFor = Exception.class)
    public void follow(Long followUserId, boolean followStatus) {
        Long userId = SessionUserHolder.getSessionUser().getId();
        if (Objects.equals(userId, followUserId)) {
            throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "自己不能关注自己");
        }
        // 关注对象, userId关注了followUserId
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setFollowUserId(followUserId);
        userFollow.setStatus(followStatus);
        userFollow.setCreateTime(LocalDateTime.now());
        userFollow.setUpdateTime(userFollow.getCreateTime());

        // 粉丝对象
        UserFollower userFollower = new UserFollower();
        userFollower.setUserId(followUserId);
        userFollow.setFollowUserId(userId);
        userFollow.setStatus(followStatus);

        userFollowMapper.follow(userFollow);
        userFollowerMapper.follower(userFollower);

        // 发布关注事件
        UserMessageDTO userMessage = new UserMessageDTO()
                .setMessageType(UserCenterMessageTypeEnum.FOLLOW.getValue())
                .setServiceMessageType(UserCenterServiceMessageTypeEnum.FOLLOW.getValue())
                .setItemId(userFollow.getId())
                .setPostId(null)
                .setContent(null)
                .setToUserId(followUserId)
                .setFromUserId(userId);
        applicationContext.publishEvent(new FollowEvent(this, userMessage));
    }

    /**
     * 关注列表
     */
    public IPage<UserFollowVO> listFollow(UserFollowDTO request) {
        Page<UserFollow> page = ChainWrappers.lambdaQueryChain(userFollowMapper)
                .eq(UserFollow::getStatus, Boolean.TRUE)
                .eq(UserFollow::getUserId, request.getUserId())
                .page(new Page<>(request.getCurrent(), request.getSize()));
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            Set<Long> userIds = page.getRecords().stream()
                    .map(UserFollow::getFollowUserId)
                    .collect(Collectors.toSet());
            Map<Long, UserVO> userMap = userFeignClient.getByIds(userIds);
            return page.convert(userFollow -> {
                UserFollowVO userFollowVO = new UserFollowVO();
                userFollowVO.setUserId(userFollow.getFollowUserId());
                UserVO user = userMap.get(userFollow.getFollowUserId());
                if (user != null) {
                    userFollowVO.setAvatarUrl(user.getAvatarUrl());
                    userFollowVO.setNickName(user.getNickName());
                }
                return userFollowVO;
            });
        }
        return new Page<>();
    }

    /**
     * 粉丝列表
     */
    public IPage<UserFollowVO> listFollower(UserFollowDTO request) {
        Page<UserFollower> page = ChainWrappers.lambdaQueryChain(userFollowerMapper)
                .eq(UserFollower::getStatus, Boolean.TRUE)
                .eq(UserFollower::getUserId, request.getUserId())
                .page(new Page<>(request.getCurrent(), request.getSize()));
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            Set<Long> userIds = page.getRecords().stream()
                    .map(UserFollower::getFollowerId)
                    .collect(Collectors.toSet());
            Map<Long, UserVO> userMap = userFeignClient.getByIds(userIds);
            return page.convert(userFollower -> {
                UserFollowVO userFollowVO = new UserFollowVO();
                userFollowVO.setUserId(userFollower.getFollowerId());
                UserVO user = userMap.get(userFollower.getFollowerId());
                if (user != null) {
                    userFollowVO.setAvatarUrl(user.getAvatarUrl());
                    userFollowVO.setNickName(user.getNickName());
                }
                return userFollowVO;
            });
        }
        return new Page<>();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
