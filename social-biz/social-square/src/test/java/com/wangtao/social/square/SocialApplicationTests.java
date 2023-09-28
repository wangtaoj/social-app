package com.wangtao.social.square;

import com.wangtao.social.square.mapper.LikeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangtao
 * Created at 2023-09-28
 */
@SpringBootTest
public class SocialApplicationTests {

    @Autowired
    private LikeMapper likeMapper;

    @Test
    public void contextLoad() {
        System.out.println(likeMapper.selectById(1706668982498504707L));
    }
}
