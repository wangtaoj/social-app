package com.wangtao.social.common.core.session;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;

import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
public class SessionUserHolder {

    private static final InheritableThreadLocal<SessionUser> SESSION = new InheritableThreadLocal<>();

    public static SessionUser getSessionUser() {
        SessionUser sessionUser = SESSION.get();
        if (Objects.isNull(sessionUser)) {
            throw new BusinessException(ResponseEnum.AUTH_FAIL, "您还未登录!");
        }
        return sessionUser;
    }

    static void set(SessionUser sessionUser) {
        SESSION.set(Objects.requireNonNull(sessionUser));
    }

    static void remove() {
        SESSION.remove();
    }
}
