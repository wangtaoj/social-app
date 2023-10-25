package com.wangtao.social.api.square.feign;

import com.wangtao.social.api.square.vo.FollowStatisticsVO;
import com.wangtao.social.common.core.constant.UrlConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangtao
 * Created at 2023-10-25
 */
@FeignClient(value = "social-square", path = FollowFeignClient.PREFIX)
public interface FollowFeignClient {

    String PREFIX = UrlConstant.FEGIN_PREFIX + "/follow";

    /**
     * 获取指定用户的关注信息
     * @param userId 用户id
     * @param loginUserId 当前登录用户id
     * @return 关注信息
     */
    @PostMapping("/getFollowStatisticsInfo")
    FollowStatisticsVO getFollowStatisticsInfo(@RequestParam("userId") Long userId,
                                               @RequestParam("loginUserId") Long loginUserId);
}
