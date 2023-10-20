package com.wangtao.social.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.user.api.dto.UserMessageDTO;
import com.wangtao.social.user.api.vo.MessageStatisticsVO;
import com.wangtao.social.user.api.vo.UserMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Service
public class UserCenterService {

    @Autowired
    private UserInboxService userInboxService;

    public MessageStatisticsVO messageStatistics() {
        Long userId = SessionUserHolder.getSessionUser().getId();
        MessageStatisticsVO statisticsVO = new MessageStatisticsVO();
        statisticsVO.setLikeCount(userInboxService.getUnReadLikeCount(userId));
        statisticsVO.setCommentCount(userInboxService.getUnReadCommentCount(userId));
        statisticsVO.setFollowCount(userInboxService.getUnReadFollowCount(userId));
        // TODO: 系统消息
        statisticsVO.setTotal(statisticsVO.getLikeCount() + statisticsVO.getCommentCount() + statisticsVO.getSysCount()+ statisticsVO.getFollowCount());
        return statisticsVO;
    }

    public IPage<UserMessageVO> listUserMessage(UserMessageDTO messageDTO) {
        return userInboxService.list(messageDTO);
    }
}
