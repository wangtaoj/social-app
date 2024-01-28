package com.wangtao.social.common.core.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangtao
 * Created at 2023-09-27
 */
@Setter
@Getter
public class PageParam {

    /**
     * 页大小
     */
    private Long size = 10L;

    /**
     * 当前页
     */
    private Long current = 1L;

    public Long offset() {
        return (current - 1) * size;
    }
}
