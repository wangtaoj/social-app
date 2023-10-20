package com.wangtao.social.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.common.user.enums.UserCenterMessageTypeEnum;
import com.wangtao.social.user.api.dto.UserMessageDTO;
import com.wangtao.social.user.api.vo.UserMessageVO;
import com.wangtao.social.user.converter.UserInboxConverter;
import com.wangtao.social.user.mapper.UserInboxMapper;
import com.wangtao.social.user.po.UserInbox;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Service
public class UserInboxService {

    @Autowired
    private UserInboxMapper userInboxMapper;

    @Autowired
    private UserInboxConverter userInboxConverter;

    public void consumeMessage(com.wangtao.social.common.user.dto.UserMessageDTO  userMessage) {
        UserInbox userInbox = userInboxConverter.convert(userMessage);
        userInboxMapper.insert(userInbox);
    }

    @SuppressWarnings("unchecked")
    private long getReadPositionId(Long userId, UserCenterMessageTypeEnum messageType) {
        return ChainWrappers.lambdaQueryChain(userInboxMapper)
                .select(UserInbox::getReadPositionId)
                .eq(UserInbox::getToUserId, userId)
                .eq(UserInbox::getMessageType, messageType.getValue())
                .gt(UserInbox::getReadPositionId, 0)
                .orderByDesc(UserInbox::getId)
                .last("limit 1").oneOpt()
                .map(UserInbox::getReadPositionId).orElse(0L);
    }

    private int getUnReadMessageCount(Long userId, UserCenterMessageTypeEnum messageType, long readPositionId) {
        return ChainWrappers.lambdaQueryChain(userInboxMapper)
                .eq(UserInbox::getToUserId, userId)
                .eq(UserInbox::getMessageType, messageType.getValue())
                .gt(readPositionId > 0, UserInbox::getReadPositionId, readPositionId)
                .count().intValue();
    }

    /**
     * 获取用户未读的点赞消息数
     */
    public Integer getUnReadLikeCount(Long userId) {
        long readPositionId = getReadPositionId(userId, UserCenterMessageTypeEnum.LIKE);
        return getUnReadMessageCount(userId, UserCenterMessageTypeEnum.LIKE, readPositionId);
    }

    /**
     * 获取用户未读的评论消息数
     */
    public Integer getUnReadCommentCount(Long userId) {
        long readPositionId = getReadPositionId(userId, UserCenterMessageTypeEnum.COMMENT);
        return getUnReadMessageCount(userId, UserCenterMessageTypeEnum.COMMENT, readPositionId);
    }

    /**
     * 获取用户未读的关注消息数
     */
    public Integer getUnReadFollowCount(Long userId) {
        long readPositionId = getReadPositionId(userId, UserCenterMessageTypeEnum.FOLLOW);
        return getUnReadMessageCount(userId, UserCenterMessageTypeEnum.FOLLOW, readPositionId);
    }

    public IPage<UserMessageVO> list(UserMessageDTO messageDTO) {
        IPage<UserMessageVO> page = userInboxMapper.listByUserIdAndMessageType(
                new Page<>(messageDTO.getCurrent(), messageDTO.getSize()),
                SessionUserHolder.getSessionUser().getId(),
                messageDTO.getMessageType().getValue()
        );
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            Long id = page.getRecords().get(0).getId();
            // 更新读取位置, 取最大的一条
            ChainWrappers.lambdaUpdateChain(userInboxMapper)
                    .set(UserInbox::getReadPositionId, id)
                    .set(UserInbox::getUpdateTime, LocalDateTime.now())
                    .eq(UserInbox::getId, id)
                    .update();
        }
        return page;
    }
}
