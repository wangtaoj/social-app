package com.wangtao.social.common.canal.processor;

import com.wangtao.social.common.canal.constant.CanalEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023/4/1 15:18
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class CanalResult<T> {

    private T beforeData;

    private T afterData;

    private CanalEventType canalEventType;
}
