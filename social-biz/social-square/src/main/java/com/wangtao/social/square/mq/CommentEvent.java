package com.wangtao.social.square.mq;

import com.wangtao.social.common.user.dto.UserMessageDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author wangtao
 * Created at 2023-10-18
 */
@Getter
public class CommentEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2959192226680744204L;

    private final UserMessageDTO userMessage;

    private final boolean async;

    public CommentEvent(Object source, UserMessageDTO userMessage) {
        this(source, userMessage, true);
    }

    public CommentEvent(Object source, UserMessageDTO userMessage, boolean async) {
        super(source);
        this.userMessage = userMessage;
        this.async = async;
    }
}
