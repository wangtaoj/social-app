package com.wangtao.social.square.api.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author wangtao
 * Created at 2023-10-07
 */
@Accessors(chain = true)
@ToString
@Setter
@Getter
public class UserPostStatisticsVO {

    /**
     * 用户帖子数量
     */
    private Long postCount;

    /**
     * 用户帖子点赞数量
     */
    private Long postLikeCount;
}
