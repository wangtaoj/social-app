package com.wangtao.social.square.api.feign;

import com.wangtao.social.api.square.feign.FollowFeignClient;
import com.wangtao.social.api.square.vo.FollowStatisticsVO;
import com.wangtao.social.square.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 * Created at 2023-10-25
 */
@RequestMapping(FollowFeignClient.PREFIX)
@RestController
public class FollowFeignController implements FollowFeignClient {

    @Autowired
    private UserFollowService userFollowService;

    @Override
    public FollowStatisticsVO getFollowStatisticsInfo(Long userId, Long loginUserId) {
        return userFollowService.getFollowStatistics(userId, loginUserId);
    }
}
