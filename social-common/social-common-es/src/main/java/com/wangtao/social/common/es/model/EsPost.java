package com.wangtao.social.common.es.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2024-01-27
 */
@ToString
@Setter
@Getter
public class EsPost {

    /**
     * 帖子id
     */
    private Long id;

    /**
     * 发帖人
     */
    @JsonAlias("user_id")
    private Long userId;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 删除标志
     */
    @JsonAlias("del_flg")
    private Integer delFlg;
}
