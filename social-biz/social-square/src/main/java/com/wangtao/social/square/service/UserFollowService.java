package com.wangtao.social.square.service;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.common.user.dto.UserMessageDTO;
import com.wangtao.social.common.user.enums.UserCenterMessageTypeEnum;
import com.wangtao.social.common.user.enums.UserCenterServiceMessageTypeEnum;
import com.wangtao.social.square.mapper.UserFollowMapper;
import com.wangtao.social.square.mapper.UserFollowerMapper;
import com.wangtao.social.square.mq.FollowEvent;
import com.wangtao.social.square.po.UserFollow;
import com.wangtao.social.square.po.UserFollower;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
