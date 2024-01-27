package com.wangtao.social.common.es.constant;

import lombok.Getter;

/**
 * @author wangtao
 * Created at 2024-01-27
 */
@Getter
public enum EsIndexEnum {

    POST("post", "帖子"),
    ;

    /**
     * 索引名称
     */
    private final String name;

    /**
     * 索引描述
     */
    private final String desp;

    EsIndexEnum(String name, String desp) {
        this.name = name;
        this.desp = desp;
    }
}
