package com.wangtao.social.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.user.api.dto.UserMessageDTO;
import com.wangtao.social.user.api.vo.UserMessageVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@Service
public class UserCenterService {

    @Autowired
    private UserInboxService userInboxService;

    public IPage<UserMessageVO> listUserMessage(UserMessageDTO messageDTO) {
        IPage<UserMessageVO> page = userInboxService.list(messageDTO);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            // TODO: 填充信息
        }
        return page;
    }
}
