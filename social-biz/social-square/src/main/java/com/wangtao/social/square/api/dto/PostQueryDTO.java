package com.wangtao.social.square.api.dto;

import com.wangtao.social.common.core.dto.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-09-27
 */
@ToString
@Setter
@Getter
public class PostQueryDTO extends PageParam {

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 前一个帖子id，用于游标翻页
     */
    private Long postIdBefore;

    /**
     * 用户id
     */
    private Long userId;

}
