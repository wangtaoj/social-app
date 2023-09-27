package com.wangtao.social.square.api.dto;

import com.wangtao.social.common.core.dto.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 目前只支持降序
     */
    public String getOrderBy() {
        String rule = "desc";
        if ("id".equalsIgnoreCase(column)) {
            return column + " " + rule;
        }
        return column + " " + rule + ", id desc";
    }

}
