package com.wangtao.social.square.converter;

import com.wangtao.social.square.api.dto.AddPostDTO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.po.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, typeConversionPolicy = ReportingPolicy.ERROR)
public interface PostConverter {

    Post convert(AddPostDTO addPostDTO);

    PostVO convertToVO(Post post);
}
