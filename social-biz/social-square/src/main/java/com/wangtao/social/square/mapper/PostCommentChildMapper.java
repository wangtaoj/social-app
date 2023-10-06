package com.wangtao.social.square.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.square.api.dto.PostCommentQueryDTO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.po.PostCommentChild;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@Mapper
public interface PostCommentChildMapper extends BaseMapper<PostCommentChild> {

    IPage<CommentVO> listByParentId(IPage<CommentVO> page, @Param("query") PostCommentQueryDTO query);

    int countByItemId(@Param("itemId") Long itemId);
}




