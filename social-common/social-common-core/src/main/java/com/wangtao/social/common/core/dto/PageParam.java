package com.wangtao.social.common.core.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 需要进行排序的字段
     */
    private String column;

    /**
     * 自然排序(正序)：由小到大，asc，“-” 号开头：true
     * 倒序：由大到小，desc，“+”号开头：false
     */
    private boolean asc = true;

    public void setColumn(String column) {
        if (StringUtils.isBlank(column)) {
            return;
        }
        if (column.startsWith("+")) {
            this.asc = false;
        }
        this.column = column.substring(1);
    }
}
