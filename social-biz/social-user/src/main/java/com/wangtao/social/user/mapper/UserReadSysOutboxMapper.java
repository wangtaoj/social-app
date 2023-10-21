package com.wangtao.social.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangtao.social.user.po.UserReadSysOutbox;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangtao
 * Created at 2023-10-20
 */
@Mapper
public interface UserReadSysOutboxMapper extends BaseMapper<UserReadSysOutbox> {

    void updateReadLog(UserReadSysOutbox readSysOutbox);
}




