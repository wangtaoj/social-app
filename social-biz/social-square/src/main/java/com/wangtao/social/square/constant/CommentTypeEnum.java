package com.wangtao.social.square.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@Getter
public enum CommentTypeEnum {

    POST(3, "帖子"),

    POST_COMMENT(2, "帖子评论"),
    ;

    @EnumValue
    private final Integer value;

    private final String desp;

    CommentTypeEnum(Integer value, String desp) {
        this.value = value;
        this.desp = desp;
    }
}
