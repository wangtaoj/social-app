package com.wangtao.social.square.service;

import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.square.api.dto.AddPostDTO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.converter.PostConverter;
import com.wangtao.social.square.mapper.PostMapper;
import com.wangtao.social.square.po.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@Service
public class PostService {

    @Autowired
    private PostConverter postConverter;

    @Autowired
    private PostMapper postMapper;

    public PostVO addPost(AddPostDTO postDTO) {
        Post post = postConverter.convert(postDTO);
        post.setUserId(SessionUserHolder.getSessionUser().getId());
        postMapper.insert(post);

        PostVO postVO = postConverter.convertToVO(post);
        postVO.setLike(false);
        postVO.setCommentCount(0);
        return postConverter.convertToVO(post);
    }
}
