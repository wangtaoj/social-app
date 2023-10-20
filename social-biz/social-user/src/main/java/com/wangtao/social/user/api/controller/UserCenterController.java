package com.wangtao.social.user.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.common.core.response.ServerReponseDecorator;
import com.wangtao.social.user.api.dto.UserMessageDTO;
import com.wangtao.social.user.api.vo.MessageStatisticsVO;
import com.wangtao.social.user.api.vo.UserMessageVO;
import com.wangtao.social.user.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 * Created at 2023-10-16
 */
@ServerReponseDecorator
@RequestMapping("/user/center")
@RestController
public class UserCenterController {

    @Autowired
    private UserCenterService userCenterService;

    /**
     * 用户未读消息统计
     *
     * @return 未读消息统计
     */
    @GetMapping("/messageStatistics")
    public MessageStatisticsVO messageStatistics() {
        return userCenterService.messageStatistics();
    }

    /**
     * 分页查询用户消息
     * @param messageDTO 参数
     * @return 消息列表
     */
    @PostMapping("/listUserMessage")
    public IPage<UserMessageVO> listUserMessage(@Validated @RequestBody UserMessageDTO messageDTO) {
        return userCenterService.listUserMessage(messageDTO);
    }
}
