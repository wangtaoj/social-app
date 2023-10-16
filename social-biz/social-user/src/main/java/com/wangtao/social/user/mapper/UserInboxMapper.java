package com.wangtao.social.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.user.api.vo.UserMessageVO;
import com.wangtao.social.user.po.UserInbox;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Mapper
public interface UserInboxMapper extends BaseMapper<UserInbox> {

    /**
     * 根据用户id和消息类型查询
     * @param toUserId 用户id
     * @param messageType 消息类型
     * @return 消息列表
     */
    IPage<UserMessageVO> listByUserIdAndMessageType(IPage<UserMessageVO> page,
                                                    @Param("toUserId") Long toUserId,
                                                    @Param("messageType") Integer messageType);
}




