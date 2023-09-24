package com.wangtao.social.user.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.common.redis.util.RedisKeyUtils;
import com.wangtao.social.user.domain.SysUser;
import com.wangtao.social.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Service
public class SysUserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper;

    public SysUser selectByPhone(String phone) {
        Wrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone);
        return sysUserMapper.selectOne(queryWrapper);
    }

    public int insert(SysUser sysUser) {
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        return sysUserMapper.insert(sysUser);
    }

    public void logout() {
        String token = SessionUserHolder.getToken();
        String sessionKey = RedisKeyUtils.getSessionKey(token);
        Boolean delete = redisTemplate.delete(sessionKey);
        if (delete == null || !delete) {
            throw new BusinessException(ResponseEnum.LOGOUT_FAIL);
        }
    }
}
