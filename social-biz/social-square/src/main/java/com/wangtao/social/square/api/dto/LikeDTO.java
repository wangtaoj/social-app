package com.wangtao.social.square.api.dto;

import com.wangtao.social.common.square.enums.LikeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author wangtao
 * Created at 2023-09-28
 */
@ToString
@Setter
@Getter
public class LikeDTO {

    /**
     * 点赞条目id
     */
    @NotNull(message = "点赞条目不能为空")
    private Long itemId;

    /**
     * 点赞状态
     */
    @NotNull(message = "点赞状态不能为空")
    private Boolean like;

    /**
     * 点赞类型
     */
    @NotNull(message = "点赞类型不能为空")
    private LikeTypeEnum type;

    /**
     * 用户id
     */
    private Long userId;
}
