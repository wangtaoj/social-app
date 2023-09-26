package com.wangtao.social.user;

import cn.hutool.core.lang.Assert;
import com.wangtao.social.common.core.util.JsonUtils;
import com.wangtao.social.user.api.dto.UserDTO;
import org.junit.jupiter.api.Test;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
public class JackSonTest {

    @Test
    public void testApi() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setPassword("123456");
        userDTO.setPhone("1570034xxxx");
        String result = JsonUtils.objToJson(userDTO);
        Assert.isFalse(result.contains("password"));
        System.out.println(JsonUtils.objToJson(userDTO));

        String json = "{\"id\":1,\"phone\":\"1570034xxxx\",\"password\":\"1234\"}";
        userDTO = JsonUtils.jsonToObj(json, UserDTO.class);
        Assert.equals("1234", userDTO.getPassword());
    }
}
