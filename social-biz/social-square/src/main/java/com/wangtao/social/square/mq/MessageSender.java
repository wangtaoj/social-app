package com.wangtao.social.square.mq;

import com.social.common.rocketemq.constant.TopicConstant;
import com.wangtao.social.common.core.util.AssertUtils;
import com.wangtao.social.common.core.util.UuidUtils;
import com.wangtao.social.common.user.dto.UserMessageDTO;
import com.wangtao.social.common.user.enums.UserCenterServiceMessageTypeEnum;
import com.wangtao.social.square.po.Post;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import com.wangtao.social.square.service.PostCommentService;
import com.wangtao.social.square.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author wangtao
 * Created at 2023-10-18
 */
@Slf4j
@Component
public class MessageSender {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private PostService postService;

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private ExecutorService mqSenderExecutor;

    /**
     * 事务提交后执行
     */
    @TransactionalEventListener(fallbackExecution = true)
    public void sendLikeMessage(LikeEvent likeEvent) {
        UserMessageDTO userMessage = likeEvent.getUserMessage();
        try {
            /*
             * 事务钩子执行时, 异常会被吞掉, 因此手动try catch
             * 参见TransactionalApplicationListenerSynchronization.afterCompletion
             * TransactionSynchronizationUtils.triggerAfterCompletion
             */
            if (likeEvent.isAsync()) {
                mqSenderExecutor.execute(() -> doSendLikeMessage(userMessage));
            } else {
                doSendLikeMessage(userMessage);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void doSendLikeMessage(UserMessageDTO userMessage) {
        if (UserCenterServiceMessageTypeEnum.POST.getValue().equals(userMessage.getServiceMessageType())) {
            Post post = postService.getPost(userMessage.getItemId());
            if (Objects.nonNull(post)) {
                userMessage.setPostId(post.getId());
                userMessage.setToUserId(post.getUserId());
            }
        }
        else if (UserCenterServiceMessageTypeEnum.POST_ONE_COMMENT.getValue().equals(userMessage.getServiceMessageType())) {
            // 点赞一级评论
            PostCommentParent postCommentParent = postCommentService.getPostCommentParent(userMessage.getItemId());
            if (Objects.nonNull(postCommentParent)) {
                userMessage.setPostId(postCommentParent.getItemId());
                userMessage.setContent(postCommentParent.getContent());
                userMessage.setToUserId(postCommentParent.getUserId());
            }
        } else if (UserCenterServiceMessageTypeEnum.POST_TWO_COMMENT.getValue().equals(userMessage.getServiceMessageType())) {
            // 点赞二级评论
            PostCommentChild postCommentChild = postCommentService.getPostCommentChild(userMessage.getItemId());
            if (Objects.nonNull(postCommentChild)) {
                userMessage.setPostId(postCommentChild.getItemId());
                userMessage.setContent(postCommentChild.getContent());
                userMessage.setToUserId(postCommentChild.getUserId());
            }
        }
        sendMessage(TopicConstant.USER_MESSAGE + ":" + TopicConstant.TAG_LIKE, userMessage);
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void sendCommentMessage(CommentEvent commentEvent) {
        UserMessageDTO userMessage = commentEvent.getUserMessage();
        try {
            if (commentEvent.isAsync()) {
                mqSenderExecutor.execute(() -> doSendCommentMessage(userMessage));
            } else {
                doSendCommentMessage(userMessage);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void doSendCommentMessage(UserMessageDTO userMessage) {
        sendMessage(TopicConstant.USER_MESSAGE + ":" + TopicConstant.TAG_COMMENT, userMessage);
    }

    private void sendMessage(String topic, UserMessageDTO userMessage) {
        userMessage.setUuid(UuidUtils.uuid());
        checkMessage(userMessage);
        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageConst.PROPERTY_KEYS, userMessage.getUuid());
        rocketMQTemplate.convertAndSend(topic, userMessage, headers);
    }

    private void checkMessage(UserMessageDTO userMessage) {
        AssertUtils.assertNotEmpty(userMessage.getUuid(), "消息唯一编号不能为空");
        AssertUtils.assertNotNull(userMessage.getMessageType(), "消息类型不为空！");
        AssertUtils.assertNotNull(userMessage.getItemId(), "业务数据ID不为空！");
        AssertUtils.assertNotNull(userMessage.getServiceMessageType(), "业务数据类型不为空！");
        AssertUtils.assertNotNull(userMessage.getFromUserId(), "发起方用户ID不为空！");
        AssertUtils.assertNotNull(userMessage.getToUserId(), "接收方用户ID不为空！");
    }
}
