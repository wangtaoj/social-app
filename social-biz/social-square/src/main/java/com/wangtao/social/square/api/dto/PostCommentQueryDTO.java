package com.wangtao.social.square.api.dto;

import com.wangtao.social.common.core.dto.PageParam;
import com.wangtao.social.common.square.enums.CommentTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@ToString
@Setter
@Getter
public class PostCommentQueryDTO extends PageParam {

    /**
     * 评论条目id
     */
    @NotNull(message = "评论条目id不为空")
    private Long itemId;

    /**
     * 上一次id，分页情况下
     */
    private Long idBefore;

    /**
     * 评论类型
     */
    @NotNull(message = "评论类型不为空")
    private CommentTypeEnum type;

    /**
     * 评论的父级评论id
     */
    private Long parentId;
}
