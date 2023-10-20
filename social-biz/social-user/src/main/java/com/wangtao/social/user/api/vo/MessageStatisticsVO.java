package com.wangtao.social.user.api.vo;

import lombok.Data;

/**
 * @author wangtao
 * Created at 2023-10-19
 */
@Data
public class MessageStatisticsVO {

    /**
     * 未读点赞消息数量
     */
    private Integer likeCount = 0;

    /**
     * 未读评论消息数量
     */
    private Integer commentCount = 0;

    /**
     * 系统消息数量
     */
    private Integer sysCount = 0;

    /**
     * 未读关注消息数量
     */
    private Integer followCount = 0;

    /**
     * 总和
     */
    private Integer total = 0;
}
