package com.wangtao.social.square.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangtao.social.api.user.feign.UserFeignClient;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.square.api.dto.AddPostDTO;
import com.wangtao.social.square.api.dto.PostQueryDTO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.converter.PostConverter;
import com.wangtao.social.square.mapper.PostMapper;
import com.wangtao.social.square.po.Post;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private UserFeignClient userFeignClient;

    public PostVO addPost(AddPostDTO postDTO) {
        Post post = postConverter.convert(postDTO);
        post.setUserId(SessionUserHolder.getSessionUser().getId());
        postMapper.insert(post);

        PostVO postVO = postConverter.convertToVO(post);
        postVO.setLike(false);
        postVO.setCommentCount(0);
        return postConverter.convertToVO(post);
    }

    public IPage<PostVO> list(PostQueryDTO postQuery) {
        // 检查排序字段
        if (!"id".equalsIgnoreCase(postQuery.getColumn()) && !"like_count".equalsIgnoreCase(postQuery.getColumn())) {
            throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "只能根据时间和点赞数量排序");
        }
        IPage<PostVO> page = new Page<>(postQuery.getCurrent(), postQuery.getSize());
        postMapper.list(page, postQuery);
        fillExtraInfo(page);
        return page;
    }

    public IPage<PostVO> listMyPost(PostQueryDTO postQuery) {
        postQuery.setUserId(SessionUserHolder.getSessionUser().getId());
        IPage<Post> tmpPage = new LambdaQueryChainWrapper<>(postMapper)
                .eq(Post::getUserId, postQuery.getUserId())
                .orderByDesc(Post::getCreateTime)
                .page(new Page<>(postQuery.getCurrent(), postQuery.getSize()));

        IPage<PostVO> page = tmpPage.convert(postConverter::convertToVO);
        fillExtraInfo(page);
        return page;
    }

    private void fillExtraInfo(IPage<PostVO> page) {
        Set<Long> userIds = page.getRecords().stream()
                .map(PostVO::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> users;
        if (CollectionUtils.isEmpty(userIds)) {
            users = Collections.emptyMap();
        } else {
            users = userFeignClient.getByIds(userIds);
        }
        page.getRecords().forEach(post -> {
            // 填充用户信息
            Optional.ofNullable(users.get(post.getUserId()))
                    .ifPresent(user -> {
                        post.setNickName(user.getNickName());
                        post.setAvatarUrl(user.getAvatarUrl());
                    });
        });
    }

}
