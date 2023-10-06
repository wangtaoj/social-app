package com.wangtao.social.square.converter;

import com.wangtao.social.square.api.dto.PostCommentDTO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, typeConversionPolicy = ReportingPolicy.ERROR)
public interface PostCommentConverter {

    @Mapping(source = "postId", target = "itemId")
    PostCommentParent convertToCommentParent(PostCommentDTO dto);

    @Mapping(source = "postId", target = "itemId")
    PostCommentChild convertToCommentChild(PostCommentDTO dto);

    CommentVO convertToVO(PostCommentParent postCommentParent);

    CommentVO convertToVO(PostCommentChild postCommentChild);
}
