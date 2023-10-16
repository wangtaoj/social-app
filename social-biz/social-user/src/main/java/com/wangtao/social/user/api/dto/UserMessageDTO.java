package com.wangtao.social.user.api.dto;

import com.wangtao.social.common.core.dto.PageParam;
import com.wangtao.social.common.user.enums.UserCenterMessageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@ToString
@Setter
@Getter
public class UserMessageDTO extends PageParam {

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private UserCenterMessageTypeEnum messageType;
}
