package com.wangtao.social.user.mq;

import com.social.common.rocketemq.constant.TopicConstant;
import com.wangtao.social.common.user.dto.UserMessageDTO;
import com.wangtao.social.user.service.UserInboxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangtao
 * Created at 2023-10-18
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TopicConstant.USER_MESSAGE, consumerGroup = "user-message-consumer")
public class UserMessageListener implements RocketMQListener<UserMessageDTO> {

    @Autowired
    private UserInboxService userInboxService;

    @Override
    public void onMessage(UserMessageDTO message) {
        try {
            userInboxService.consumeMessage(message);
        } catch (Exception e) {
            log.error("consume message fail, message: {}", message, e);
        }
    }
}
