package com.wangtao.social.user.controller;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.response.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
@RestController
public class UserController {

    @GetMapping("/hello")
    public ServerResponse<Map<String, Object>> hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "wangtao");
        map.put("createTime", LocalDateTime.now());
        return ServerResponse.success(map);
    }

    @GetMapping("/exp")
    public ServerResponse<Map<String, Object>> exp(String name) {
        throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, "name is required!");
    }
}
