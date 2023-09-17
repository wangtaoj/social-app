package com.wangtao.social.common.core.util;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
public final class AssertUtils {

    private AssertUtils() {}

    public static void assertNotEmpty(Object value, String msg) {
        if (value instanceof String) {
            if (StringUtils.isNotBlank(value.toString())) {
                return;
            }
        }
        throw new BusinessException(ResponseEnum.PARAM_ILLEGAL, msg);
    }

}
