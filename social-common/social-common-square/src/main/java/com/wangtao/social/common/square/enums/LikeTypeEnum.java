package com.wangtao.social.common.square.enums;

import lombok.Getter;

/**
 * @author wangtao
 * Created at 2023-10-07
 */
@Getter
public enum LikeTypeEnum {

    POST(1, "帖子"),

    POST_COMMENT_ONE(2, "帖子一级评论"),

    POST_COMMENT_TWO(3, "帖子二级评论"),
    ;

    private final int value;

    private final String desp;

    LikeTypeEnum(int value, String desp) {
        this.value = value;
        this.desp = desp;
    }
}
