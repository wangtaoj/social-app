package com.wangtao.social.square.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangtao.social.api.user.feign.UserFeignClient;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.square.api.dto.PostCommentQueryDTO;
import com.wangtao.social.square.api.vo.CommentListVO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.mapper.PostCommentChildMapper;
import com.wangtao.social.square.mapper.PostCommentParentMapper;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangtao
 * Created at 2023-10-06
 */
@Service
public class PostCommentService {

    @Autowired
    private PostCommentParentMapper postCommentParentMapper;

    @Autowired
    private PostCommentChildMapper postCommentChildMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    public void insertPostCommentParent(PostCommentParent postCommentParent) {
        postCommentParentMapper.insert(postCommentParent);
    }

    public void insertPostCommentChild(PostCommentChild postCommentChild) {
        postCommentChildMapper.insert(postCommentChild);
    }

    public PostCommentParent getPostCommentParent(Long id) {
        return postCommentParentMapper.selectById(id);
    }

    public PostCommentChild getPostCommentChild(Long id) {
        return postCommentChildMapper.selectById(id);
    }

    public int getCommentCountOfPost(Long postId) {
        int oneComment = postCommentParentMapper.countByItemId(postId);
        int twoComment = postCommentChildMapper.countByItemId(postId);
        return oneComment + twoComment;
    }

    public CommentListVO listOneComment(PostCommentQueryDTO commentQueryDTO) {
        CommentListVO vo = new CommentListVO();
        vo.setCommentTotal(0L);
        IPage<CommentVO> commentPage = postCommentParentMapper.listByItemId(
                new Page<>(commentQueryDTO.getCurrent(), commentQueryDTO.getSize()),
                commentQueryDTO
        );
        vo.setCommentPageData(commentPage);
        if (CollectionUtils.isEmpty(vo.getCommentPageData().getRecords())) {
            return vo;
        }
        vo.setCommentTotal(vo.getCommentPageData().getTotal());
        vo.getCommentPageData().getRecords().forEach(comment -> {
            // 填充二级评论
            commentQueryDTO.setParentId(comment.getId());
            commentQueryDTO.setCurrent(1L);
            commentQueryDTO.setSize(3L);
            comment.setChildCommentPage(listTwoComment(commentQueryDTO, false));
        });
        fillUserInfo(vo.getCommentPageData().getRecords());
        return vo;
    }

    public IPage<CommentVO> listTwoComment(PostCommentQueryDTO commentQueryDTO, boolean fillUserInfo) {
        IPage<CommentVO> commentPage = postCommentChildMapper.listByParentId(
                new Page<>(commentQueryDTO.getCurrent(), commentQueryDTO.getSize()),
                commentQueryDTO
        );
        if (CollectionUtils.isEmpty(commentPage.getRecords())) {
            return commentPage;
        }

        // 填充二级评论回复信息
        Set<Long> replyIdSet = commentPage.getRecords().stream()
                .map(CommentVO::getReplyId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(replyIdSet)) {
            Map<Long, PostCommentChild> replyCommentMap = new LambdaQueryChainWrapper<>(postCommentChildMapper)
                    .select(PostCommentChild::getId, PostCommentChild::getUserId, PostCommentChild::getContent)
                    .in(PostCommentChild::getId, replyIdSet)
                    .list()
                    .stream().collect(Collectors.toMap(PostCommentChild::getId, Function.identity()));
            commentPage.getRecords().forEach(childComment -> {
                if (Objects.nonNull(childComment.getReplyId())) {
                    PostCommentChild replyComment = replyCommentMap.get(childComment.getReplyId());
                    if (Objects.nonNull(replyComment)) {
                        CommentVO.ReplyInfo replyInfo = new CommentVO.ReplyInfo();
                        replyInfo.setUserId(replyComment.getUserId());
                        replyInfo.setContent(replyComment.getContent());
                        childComment.setReplyInfo(replyInfo);
                    }
                }
            });
        }

        fillUserInfo(commentPage.getRecords());
        return commentPage;
    }

    /**
     * 填充用户信息
     * 如果是一级评论, 那么下面可能有二级评论
     * 如果直接是二级评论, 则childCommentPage为空
     */
    private void fillUserInfo(List<CommentVO> commentList) {
        Set<Long> userIdSet = new HashSet<>();
        for (CommentVO comment : commentList) {
            // 一级或二级评论
            userIdSet.add(comment.getUserId());
            // 本身就是二级评论
            if (Objects.nonNull(comment.getReplyInfo())) {
                userIdSet.add(comment.getReplyInfo().getUserId());
            }
            // 一级评论下面可能存在二级评论
            if (Objects.nonNull(comment.getChildCommentPage()) &&
                    CollectionUtils.isNotEmpty(comment.getChildCommentPage().getRecords())) {
                for (CommentVO childComent : comment.getChildCommentPage().getRecords()) {
                    // 二级评论
                    userIdSet.add(childComent.getUserId());
                    // 二级评论回复信息
                    if (Objects.nonNull(childComent.getReplyInfo())) {
                        userIdSet.add(childComent.getReplyInfo().getUserId());
                    }
                }
            }
        }

        Map<Long, UserVO> userMap;
        if (CollectionUtils.isEmpty(userIdSet)) {
            userMap = Collections.emptyMap();
        } else {
            userMap = userFeignClient.getByIds(userIdSet);
        }
        for (CommentVO comment : commentList) {
            fillUserInfo(comment, userMap);
            // 二级评论
            if (Objects.nonNull(comment.getChildCommentPage()) &&
                    CollectionUtils.isNotEmpty(comment.getChildCommentPage().getRecords())) {
                for (CommentVO childComent : comment.getChildCommentPage().getRecords()) {
                    fillUserInfo(childComent, userMap);
                }
            }
        }
    }

    private void fillUserInfo(CommentVO comment, Map<Long, UserVO> userMap) {
        // 填充用户信息
        UserVO user = userMap.get(comment.getUserId());
        if (Objects.nonNull(user)) {
            comment.setNickName(user.getNickName());
            comment.setAvatarUrl(user.getAvatarUrl());
        }
        // 回复信息
        if (Objects.nonNull(comment.getReplyInfo())) {
            user = userMap.get(comment.getReplyInfo().getUserId());
            if (Objects.nonNull(user)) {
                comment.getReplyInfo().setNickName(user.getNickName());
                comment.getReplyInfo().setAvatarUrl(user.getAvatarUrl());
            }
        }
    }
}
