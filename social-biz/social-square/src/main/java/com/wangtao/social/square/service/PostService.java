package com.wangtao.social.square.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangtao.social.api.user.feign.UserFeignClient;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.common.core.constant.GlobalConstant;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.common.es.constant.EsIndexEnum;
import com.wangtao.social.common.es.model.EsPost;
import com.wangtao.social.common.user.dto.UserMessageDTO;
import com.wangtao.social.common.user.enums.UserCenterMessageTypeEnum;
import com.wangtao.social.common.user.enums.UserCenterServiceMessageTypeEnum;
import com.wangtao.social.square.api.dto.AddPostDTO;
import com.wangtao.social.square.api.dto.PostCommentDTO;
import com.wangtao.social.square.api.dto.PostQueryDTO;
import com.wangtao.social.square.api.dto.PostSearchDTO;
import com.wangtao.social.square.api.vo.CommentVO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.api.vo.UserPostStatisticsVO;
import com.wangtao.social.square.converter.PostCommentConverter;
import com.wangtao.social.square.converter.PostConverter;
import com.wangtao.social.square.mapper.PostMapper;
import com.wangtao.social.square.mq.CommentEvent;
import com.wangtao.social.square.po.Post;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@Service
public class PostService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ElasticsearchClient esClient;

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

    public Post getPost(Long id) {
        return postMapper.selectById(id);
    }

    public PostVO addPost(AddPostDTO postDTO) {
        Post post = postConverter.convert(postDTO);
        post.setUserId(SessionUserHolder.getSessionUser().getId());
        postMapper.insert(post);

        PostVO postVO = postConverter.convertToVO(post);
        postVO.setLike(false);
        postVO.setCommentCount(0);
        postVO.setLikeCount(0);
        return postVO;
    }

    public void deletePost(Long id) {
        new LambdaUpdateChainWrapper<>(postMapper)
                .eq(Post::getId, id)
                .eq(Post::getUserId, SessionUserHolder.getSessionUser().getId())
                .remove();
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
        if (Objects.isNull(postQuery.getUserId())) {
            postQuery.setUserId(SessionUserHolder.getSessionUser().getId());
        }
        IPage<Post> tmpPage = new LambdaQueryChainWrapper<>(postMapper)
                .eq(Post::getUserId, postQuery.getUserId())
                .orderByDesc(Post::getCreateTime)
                .page(new Page<>(postQuery.getCurrent(), postQuery.getSize()));

        IPage<PostVO> page = tmpPage.convert(postConverter::convertToVO);
        fillExtraInfo(page);
        return page;
    }

    public IPage<PostVO> search(PostSearchDTO postSearch) {
        IPage<EsPost> esPostPage = searchByEs(postSearch);
        if (CollectionUtils.isEmpty(esPostPage.getRecords())) {
            return new Page<>(postSearch.getCurrent(), postSearch.getSize());
        }
        Map<Long, EsPost> esPostMap = esPostPage.getRecords().stream()
                .collect(Collectors.toMap(EsPost::getId, Function.identity()));
        List<Long> ids = esPostPage.getRecords().stream().map(EsPost::getId).collect(Collectors.toList());
        List<Post> postTmps = postMapper.selectBatchIds(ids);
        // 设置高亮内容
        for (Post postTmp : postTmps) {
            EsPost esPost = esPostMap.get(postTmp.getId());
            if (Objects.nonNull(esPost)) {
                postTmp.setContent(esPost.getContent());
            }
        }

        List<PostVO> posts = postTmps.stream().map(postConverter::convertToVO).collect(Collectors.toList());
        IPage<PostVO> page = Page.of(esPostPage.getCurrent(), esPostPage.getSize(), esPostPage.getTotal());
        page.setRecords(posts);
        fillExtraInfo(page);
        return page;
    }

    private IPage<EsPost> searchByEs(PostSearchDTO postSearch) {
        List<Query> queryList = new ArrayList<>();
        Query byDelFlg = QueryBuilders.term()
                .field("delFlg")
                .value(GlobalConstant.NOT_DELETED)
                .build()._toQuery();
        queryList.add(byDelFlg);
        if (StringUtils.isNotBlank(postSearch.getKeyword())) {
            Query byContent = QueryBuilders.match()
                    .field("content")
                    .query(postSearch.getKeyword())
                    .build()._toQuery();
            queryList.add(byContent);
        }

        Query query = QueryBuilders.bool()
                .must(queryList)
                .build()._toQuery();

        SearchResponse<EsPost> response;
        try {
            response = esClient.search(
                    new SearchRequest.Builder()
                            .index(EsIndexEnum.POST.getName())
                            .query(query)
                            .from(postSearch.offset().intValue())
                            .size(postSearch.getSize().intValue())
                            .highlight(h -> h
                                    .preTags("<b style='color:red'>")
                                    .postTags("</b>")
                                    .fields("content", new HighlightField.Builder().build())
                            )
                            .build(),
                    EsPost.class
            );
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.ES_SEARCH_FAIL);
        }
        List<EsPost> esPosts = response.hits().hits().stream()
                .map(item -> {
                    EsPost esPost = item.source();
                    // 获取高亮结果
                    List<String> contents = item.highlight().get("content");
                    if (CollectionUtils.isNotEmpty(contents)) {
                        assert esPost != null;
                        esPost.setContent(contents.getFirst());
                    }
                    return esPost;
                })
                .collect(Collectors.toList());
        assert response.hits().total() != null;
        long total = response.hits().total().value();
        IPage<EsPost> page = Page.of(postSearch.getCurrent(), postSearch.getSize(), total);
        page.setRecords(esPosts);
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

    @Transactional(rollbackFor = Exception.class)
    public CommentVO addComment(PostCommentDTO request) {
        CommentVO commentVO;
        UserCenterServiceMessageTypeEnum serviceMessageTypeEnum;
        // 区分出一级评论还是二级评论
        if (Objects.isNull(request.getParentId())) {
            if (Objects.nonNull(request.getReplyId())) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "replyId must be null!");
            }
            serviceMessageTypeEnum = UserCenterServiceMessageTypeEnum.POST;
            // 一级
            commentVO = oneComment(request);
        } else if (Objects.isNull(request.getReplyId())) {
            serviceMessageTypeEnum = UserCenterServiceMessageTypeEnum.POST_ONE_COMMENT;
            // 回复 一级 的 二级 评论
            commentVO = twoComment(request);
        } else {
            serviceMessageTypeEnum = UserCenterServiceMessageTypeEnum.POST_TWO_COMMENT;
            // 回复 二级 评论的 二级 评论
            commentVO = twoComment(request);
        }
        UserMessageDTO userMessage = new UserMessageDTO()
                .setMessageType(UserCenterMessageTypeEnum.COMMENT.getValue())
                .setServiceMessageType(serviceMessageTypeEnum.getValue())
                .setPostId(commentVO.getItemId())
                .setItemId(commentVO.getId())
                .setContent(commentVO.getContent())
                .setFromUserId(commentVO.getUserId())
                .setToUserId(commentVO.getToUserId());
        applicationContext.publishEvent(new CommentEvent(this, userMessage));
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
        CommentVO commentVO = postCommentConverter.convertToVO(commentParent);
        commentVO.setToUserId(post.getUserId());
        return commentVO;
    }

    private CommentVO twoComment(PostCommentDTO request) {
        Long toUserId;
        if (Objects.nonNull(request.getReplyId())) {
            // 回复id不为null, 表明这个评论是回复二级评论的, 所以找到他要评论的信息
            PostCommentChild commentChild = postCommentService.getPostCommentChild(request.getReplyId());
            if (Objects.isNull(commentChild)) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "您要回复的评论不存在，回复失败！");
            }
            toUserId = commentChild.getUserId();
        } else {
            // 到这里, 表明回复的是一级评论
            PostCommentParent commentParent = postCommentService.getPostCommentParent(request.getParentId());
            if (Objects.isNull(commentParent)) {
                throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "您要回复的评论不存在，回复失败！");
            }
            toUserId = commentParent.getUserId();
        }

        PostCommentChild commentChild = postCommentConverter.convertToCommentChild(request);
        commentChild.setUserId(SessionUserHolder.getSessionUser().getId());
        commentChild.setLikeCount(0);
        commentChild.setPublisher(true);
        postCommentService.insertPostCommentChild(commentChild);
        CommentVO commentVO = postCommentConverter.convertToVO(commentChild);
        commentVO.setToUserId(toUserId);
        return commentVO;
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

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
