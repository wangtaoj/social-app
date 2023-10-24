package com.wangtao.social.square.api.dto;

import com.wangtao.social.common.core.dto.PageParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author wangtao
 * Created at 2023-10-24
 */
@Setter
@Getter
public class UserFollowDTO extends PageParam {

    @NotNull(message = "用户id不能为空")
    private Long userId;
}
