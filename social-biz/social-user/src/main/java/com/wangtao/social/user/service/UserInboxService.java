package com.wangtao.social.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.user.api.dto.UserMessageDTO;
import com.wangtao.social.user.api.vo.UserMessageVO;
import com.wangtao.social.user.mapper.UserInboxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Service
public class UserInboxService {

    @Autowired
    private UserInboxMapper userInboxMapper;

    public IPage<UserMessageVO> list(UserMessageDTO messageDTO) {
        return userInboxMapper.listByUserIdAndMessageType(
                new Page<>(messageDTO.getCurrent(), messageDTO.getSize()),
                SessionUserHolder.getSessionUser().getId(),
                messageDTO.getMessageType().getValue()
        );
    }
}
