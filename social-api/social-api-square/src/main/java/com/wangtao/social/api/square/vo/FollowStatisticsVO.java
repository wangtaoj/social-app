package com.wangtao.social.api.square.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangtao
 * Created at 2023-10-24
 */
@Setter
@Getter
public class FollowStatisticsVO {

    /**
     * 用户的关注数
     */
    private Integer followCount;

    /**
     * 用户的粉丝数
     */
    private Integer followerCount;

    /**
     * 登录人是否关注该用户
     */
    private Boolean follow;

}
