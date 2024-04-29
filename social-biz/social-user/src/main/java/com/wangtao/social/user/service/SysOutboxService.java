package com.wangtao.social.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.wangtao.social.common.core.session.SessionUserHolder;
import com.wangtao.social.user.mapper.SysOutboxMapper;
import com.wangtao.social.user.mapper.UserReadSysOutboxMapper;
import com.wangtao.social.user.po.SysOutbox;
import com.wangtao.social.user.po.UserReadSysOutbox;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wangtao
 * Created at 2023-10-20
 */
@Service
public class SysOutboxService {

    @Autowired
    private SysOutboxMapper sysOutboxMapper;

    @Autowired
    private UserReadSysOutboxMapper userReadSysOutboxMapper;

    private long getReadPositionId(Long userId) {
        return ChainWrappers.lambdaQueryChain(userReadSysOutboxMapper)
                .select(UserReadSysOutbox::getSysOutboxId)
                .eq(UserReadSysOutbox::getUserId, userId)
                .oneOpt().map(UserReadSysOutbox::getSysOutboxId)
                .orElse(0L);
    }

    public int getUnReadSysMsgCount(Long userId) {
        long readPositionId = getReadPositionId(userId);
        return ChainWrappers.lambdaQueryChain(sysOutboxMapper)
                .gt(SysOutbox::getId, readPositionId)
                .count().intValue();
    }

    public IPage<SysOutbox> lookSysPage(long current, long size) {
        Page<SysOutbox> page = ChainWrappers.lambdaQueryChain(sysOutboxMapper)
                .orderByDesc(SysOutbox::getId)
                .page(new Page<>(current, size));
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            // 记录一下消息读取位置，默认进来就把全部消息读完了
            if (current == 1) {
                UserReadSysOutbox userReadSysOutbox = new UserReadSysOutbox();
                userReadSysOutbox.setSysOutboxId(page.getRecords().get(0).getId());
                userReadSysOutbox.setUserId(SessionUserHolder.getSessionUser().getId());
                userReadSysOutbox.setCreateTime(LocalDateTime.now());
                userReadSysOutbox.setUpdateTime(userReadSysOutbox.getCreateTime());
                userReadSysOutboxMapper.updateReadLog(userReadSysOutbox);
            }
        }
        return page;
    }
}
