package com.wangtao.social.square.api.dto;

import com.wangtao.social.common.core.dto.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2024-01-28
 */
@ToString
@Setter
@Getter
public class PostSearchDTO extends PageParam {

    private String keyword;
}
