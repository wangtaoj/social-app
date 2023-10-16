package com.wangtao.social.common.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Getter
public enum UserCenterMessageTypeEnum {

    LIKE(1,"点赞消息"),

    COMMENT(2,"评论消息"),

    FOLLOW(3,"关注消息"),
    ;

    @EnumValue
    private final Integer value;

    private final String desp;

    UserCenterMessageTypeEnum(Integer value, String desp) {
        this.value = value;
        this.desp = desp;
    }
}
