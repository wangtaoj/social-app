package com.wangtao.social.square.service;

import com.wangtao.social.square.mapper.PostCommentChildMapper;
import com.wangtao.social.square.mapper.PostCommentParentMapper;
import com.wangtao.social.square.po.PostCommentChild;
import com.wangtao.social.square.po.PostCommentParent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
