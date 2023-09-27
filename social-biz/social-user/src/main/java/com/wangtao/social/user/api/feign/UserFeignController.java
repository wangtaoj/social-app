package com.wangtao.social.user.api.feign;

import com.wangtao.social.api.user.feign.UserFeignClient;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-27
 */
@RequestMapping(UserFeignClient.PREFIX)
@RestController
public class UserFeignController implements UserFeignClient {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Map<Long, UserVO> getByIds(Set<Long> userIds) {
        return sysUserService.getByIds(userIds);
    }
}
