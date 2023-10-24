package com.wangtao.social.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangtao.social.square.po.UserFollower;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangtao
 * Created at 2023-10-24
 */
@Mapper
public interface UserFollowerMapper extends BaseMapper<UserFollower> {

    int follower(UserFollower follower);
}




