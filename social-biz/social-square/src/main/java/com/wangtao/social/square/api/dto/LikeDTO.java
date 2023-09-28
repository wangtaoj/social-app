package com.wangtao.social.square.api.dto;

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
     * 用户id
     */
    private Long userId;
}
