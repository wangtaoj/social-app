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
import com.wangtao.social.square.api.dto.PostCommentDTO;
import com.wangtao.social.square.api.dto.PostQueryDTO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.api.vo.UserPostStatisticsVO;
import com.wangtao.social.square.converter.PostCommentConverter;
import com.wangtao.social.square.converter.PostConverter;
import com.wangtao.social.square.mapper.PostMapper;
import com.wangtao.social.square.po.Post;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
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

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private PostCommentConverter postCommentConverter;

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
        // 根据帖子id查找则不用排序
        if (postQuery.getPostId() == null) {
            // 检查排序字段
            if (!"id".equalsIgnoreCase(postQuery.getColumn()) && !"like_count".equalsIgnoreCase(postQuery.getColumn())) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "只能根据时间和点赞数量排序");
            }
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
        Set<Long> postIds = page.getRecords().stream()
                .map(PostVO::getId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userMap;
        if (CollectionUtils.isEmpty(userIds)) {
            userMap = Collections.emptyMap();
        } else {
            userMap = userFeignClient.getByIds(userIds);
        }
        Map<Long, Boolean> likeStateMap = likeService.batchGetLikeStateByItemId(postIds);
        page.getRecords().forEach(post -> {
            // 填充用户信息
            UserVO user = userMap.get(post.getUserId());
            if (Objects.nonNull(user)) {
                post.setNickName(user.getNickName());
                post.setAvatarUrl(user.getAvatarUrl());
            }
            post.setLike(likeStateMap.get(post.getId()));
            post.setCommentCount(postCommentService.getCommentCountOfPost(post.getId()));
        });
    }

    public CommentVO addComment(PostCommentDTO request) {
        CommentVO commentVO;
        // 区分出一级评论还是二级评论
        if (Objects.isNull(request.getParentId())) {
            if (Objects.nonNull(request.getReplyId())) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "replyId must be null!");
            }
            // 一级
            commentVO = oneComment(request);
        } else if (Objects.isNull(request.getReplyId())) {
            // 回复 一级 的 二级 评论
            commentVO = twoComment(request);
        } else {
            // 回复 二级 评论的 二级 评论
            commentVO = twoComment(request);
        }
        return commentVO;
    }

    private CommentVO oneComment(PostCommentDTO request) {
        Post post = postMapper.selectById(request.getPostId());
        if (Objects.isNull(post)) {
            throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "帖子数据不存在, 评论失败!");
        }
        PostCommentParent commentParent = postCommentConverter.convertToCommentParent(request);
        commentParent.setUserId(SessionUserHolder.getSessionUser().getId());
        commentParent.setLikeCount(0);
        commentParent.setPublisher(true);
        postCommentService.insertPostCommentParent(commentParent);
        return postCommentConverter.convertToVO(commentParent);
    }

    private CommentVO twoComment(PostCommentDTO request) {
        if (Objects.nonNull(request.getReplyId())) {
            // 回复id不为null, 表明这个评论是回复二级评论的, 所以找到他要评论的信息
            PostCommentChild commentChild = postCommentService.getPostCommentChild(request.getReplyId());
            if (Objects.isNull(commentChild)) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "您要回复的评论不存在，回复失败！");
            }
        } else {
            // 到这里, 表明回复的是一级评论
            PostCommentParent commentParent = postCommentService.getPostCommentParent(request.getParentId());
            if (Objects.isNull(commentParent)) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "您要回复的评论不存在，回复失败！");
            }
        }

        PostCommentChild commentChild = postCommentConverter.convertToCommentChild(request);
        commentChild.setUserId(SessionUserHolder.getSessionUser().getId());
        commentChild.setLikeCount(0);
        commentChild.setPublisher(true);
        postCommentService.insertPostCommentChild(commentChild);
        return postCommentConverter.convertToVO(commentChild);
    }

    public UserPostStatisticsVO userPostStatistics() {
        Long userId = SessionUserHolder.getSessionUser().getId();
        long postCount = new LambdaQueryChainWrapper<>(postMapper)
                .eq(Post::getUserId, userId)
                .count();

        long postLikeCount = postMapper.selectLikeCountByUserId(userId);
        return new UserPostStatisticsVO()
                .setPostCount(postCount)
                .setPostLikeCount(postLikeCount);
    }

}
