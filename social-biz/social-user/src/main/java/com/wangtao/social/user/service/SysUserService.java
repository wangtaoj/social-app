package com.wangtao.social.user.service;

import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wangtao.social.api.square.feign.FollowFeignClient;
import com.wangtao.social.api.square.vo.FollowStatisticsVO;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.common.redis.util.RedisKeyUtils;
import com.wangtao.social.user.api.dto.UserDTO;
import com.wangtao.social.user.converter.UserConverter;
import com.wangtao.social.user.mapper.SysUserMapper;
import com.wangtao.social.user.po.SysUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FollowFeignClient followFeignClient;

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

    public UserDTO info() {
        Long id = SessionUserHolder.getSessionUser().getId();
        SysUser sysUser = sysUserMapper.selectById(id);
        return userConverter.convertToDTO(sysUser);
    }

    public UserDTO infoById(Long id) {
        Long loginUserId = SessionUserHolder.getSessionUser().getId();
        SysUser sysUser = sysUserMapper.selectById(id);
        UserDTO userDTO = userConverter.convertToDTO(sysUser);
        // 中间数字变成*
        userDTO.setPhone(DesensitizedUtil.mobilePhone(userDTO.getPhone()));
        FollowStatisticsVO followStatistics = followFeignClient.getFollowStatisticsInfo(id, loginUserId);
        userDTO.setFollowInfo(followStatistics);
        return userDTO;
    }

    public void updateInfo(UserDTO user) {
        SysUser updateUser = userConverter.convert(user);
        updateUser.setId(SessionUserHolder.getSessionUser().getId());
        if (StringUtils.isNotBlank(user.getPassword())) {
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 防止空字符串
            updateUser.setPassword(null);
        }
        int updateCount = sysUserMapper.updateById(updateUser);
        if (updateCount == 0) {
            throw new BusinessException(ResponseEnum.SYS_ERROR, "用户信息不存在, 更新异常!");
        }
    }

    public Map<Long, UserVO> getByIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        return sysUserMapper.selectByIds(userIds);
    }
}
