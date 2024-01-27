package com.wangtao.social.common.canal.processor;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.wangtao.social.common.canal.constant.CanalTableModel;


/**
 * @author wangtao
 * Created at 2023/4/1 15:21
 */
public interface ICanalProcessor {

    void process(FlatMessage flatMessage);

    CanalTableModel support();
}
