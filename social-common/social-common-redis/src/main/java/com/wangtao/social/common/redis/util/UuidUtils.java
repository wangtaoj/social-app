package com.wangtao.social.common.redis.util;

import java.util.UUID;

/**
 * @author wangtao
 * Created at 2023/6/29 21:06
 */
public final class UuidUtils {

    private UuidUtils() {}

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
