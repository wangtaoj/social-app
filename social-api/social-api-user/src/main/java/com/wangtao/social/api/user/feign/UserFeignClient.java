package com.wangtao.social.api.user.feign;

import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.common.core.constant.UrlConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-27
 */
@FeignClient(value = "social-user", path = UserFeignClient.PREFIX)
public interface UserFeignClient {

    String PREFIX = UrlConstant.FEGIN_PREFIX + "/user";

    /**
     * 批量获取用户信息
     * @param userIds 用户id列表
     * @return 用户信息
     */
    @PostMapping("/getByIds")
    Map<Long, UserVO> getByIds(@RequestBody Set<Long> userIds);

}
