package com.wangtao.social.square.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.square.api.dto.LikeDTO;
import com.wangtao.social.square.mapper.LikeMapper;
import com.wangtao.social.square.mapper.PostCommentChildMapper;
import com.wangtao.social.square.mapper.PostCommentParentMapper;
import com.wangtao.social.square.mapper.PostMapper;
import com.wangtao.social.square.po.Like;
import com.wangtao.social.square.po.Post;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-28
 */
@Service
public class LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostCommentParentMapper postCommentParentMapper;

    @Autowired
    private PostCommentChildMapper postCommentChildMapper;

    @Transactional
    public void likeByDb(LikeDTO likeDTO) {
        Long userId = SessionUserHolder.getSessionUser().getId();
        Like existInfo = new LambdaQueryChainWrapper<>(likeMapper)
                .eq(Like::getItemId, likeDTO.getItemId())
                .eq(Like::getUserId, userId)
                .one();
        if (likeDTO.getLike() &&
                (Objects.nonNull(existInfo) && existInfo.getLike())) {
            throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "您已经点过赞了, 不能重复点赞！");
        }
        if (!likeDTO.getLike() &&
                (Objects.isNull(existInfo) || !existInfo.getLike())) {
            throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "您还未曾点赞过, 无法取消点赞！");
        }

        boolean isLike;
        if (Objects.isNull(existInfo)) {
            isLike = true;
            Like like = new Like();
            like.setItemId(likeDTO.getItemId());
            like.setUserId(userId);
            like.setLike(true);
            likeMapper.insert(like);
        } else {
            isLike = !existInfo.getLike();
            new LambdaUpdateChainWrapper<>(likeMapper)
                    .set(Like::getLike, isLike)
                    .eq(Like::getId, existInfo.getId())
                    .update();
        }
        switch (likeDTO.getType()) {
            case POST:
                // 更新帖子数量
                new LambdaUpdateChainWrapper<>(postMapper)
                        .setSql(isLike,"like_count = like_count + 1")
                        .setSql(!isLike, "like_count = like_count - 1")
                        .eq(Post::getId, likeDTO.getItemId())
                        .update();
                break;
            case POST_COMMENT_ONE:
                // 更新一级评论数量
                new LambdaUpdateChainWrapper<>(postCommentParentMapper)
                        .setSql(isLike,"like_count = like_count + 1")
                        .setSql(!isLike, "like_count = like_count - 1")
                        .eq(PostCommentParent::getId, likeDTO.getItemId())
                        .update();
                break;
            case POST_COMMENT_TWO:
                // 更新二级评论数量
                new LambdaUpdateChainWrapper<>(postCommentChildMapper)
                        .setSql(isLike,"like_count = like_count + 1")
                        .setSql(!isLike, "like_count = like_count - 1")
                        .eq(PostCommentChild::getId, likeDTO.getItemId())
                        .update();
                break;
            default:
                break;
        }
    }

    public Map<Long, Boolean> batchGetLikeStateByItemId(Set<Long> itemIdList) {
        Map<Long, Boolean> result = new HashMap<>();
        itemIdList.forEach(itemId -> result.put(itemId, Boolean.FALSE));

        if (CollectionUtils.isEmpty(itemIdList)) {
            return result;
        }

        // 查看数据库中是否有用户点赞记录
        new LambdaQueryChainWrapper<>(likeMapper)
                .eq(Like::getUserId, SessionUserHolder.getSessionUser().getId())
                .eq(Like::getLike, Boolean.TRUE)
                .in(Like::getItemId, itemIdList)
                .list()
                .forEach(likeObj -> {
                    if (likeObj.getLike() != null && likeObj.getLike()) {
                        result.put(likeObj.getItemId(), Boolean.TRUE);
                    }
                });
        return result;
    }

}
