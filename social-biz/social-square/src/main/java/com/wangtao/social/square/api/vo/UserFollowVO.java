package com.wangtao.social.square.api.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-10-24
 */
@ToString
@Setter
@Getter
public class UserFollowVO {

    private Long userId;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;
}
