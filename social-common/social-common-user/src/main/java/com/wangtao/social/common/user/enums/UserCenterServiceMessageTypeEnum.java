package com.wangtao.social.common.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Getter
public enum UserCenterServiceMessageTypeEnum {
    POST(1, "帖子"),

    POST_ONE_COMMENT(2, "帖子一级评论"),

    POST_TWO_COMMENT(3, "帖子二级评论"),

    FOLLOW(4, "关注"),
    ;

    @EnumValue
    private final Integer value;

    private final String desp;

    UserCenterServiceMessageTypeEnum(Integer value, String desp) {
        this.value = value;
        this.desp = desp;
    }
}
