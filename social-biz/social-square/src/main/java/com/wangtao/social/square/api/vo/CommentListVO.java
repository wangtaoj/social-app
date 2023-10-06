package com.wangtao.social.square.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@ToString
@Setter
@Getter
public class CommentListVO {

    /**
     * 评论分页数据
     */
    private IPage<CommentVO> commentPageData;

    /**
     * 评论总数(所有的, 如帖子下的评论总数等于直接(一级)评论+评论回复)
     */
    private Long commentTotal;
}
