package com.wangtao.social.square.mq;

import com.wangtao.social.common.user.dto.UserMessageDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @author wangtao
 * Created at 2023-10-18
 */
@Getter
public class FollowEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = -1704564929922868092L;

    private final UserMessageDTO userMessage;

    private final boolean async;

    public FollowEvent(Object source, UserMessageDTO userMessage) {
        this(source, userMessage, true);
    }

    public FollowEvent(Object source, UserMessageDTO userMessage, boolean async) {
        super(source);
        this.userMessage = userMessage;
        this.async = async;
    }
}
