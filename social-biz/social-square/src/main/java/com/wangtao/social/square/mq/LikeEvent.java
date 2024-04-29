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
public class LikeEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 2959192226680744204L;

    private final UserMessageDTO userMessage;

    private final boolean async;

    public LikeEvent(Object source, UserMessageDTO userMessage) {
        this(source, userMessage, true);
    }

    public LikeEvent(Object source, UserMessageDTO userMessage, boolean async) {
        super(source);
        this.userMessage = userMessage;
        this.async = async;
    }
}
