package com.wangtao.social.common.canal.constant;

import lombok.Getter;

import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023/3/31 21:45
 */
@Getter
public enum CanalEventType {

    INSERT("INSERT"),

    UPDATE("UPDATE"),

    DELETE("DELETE"),
    ;

    private final String type;

    CanalEventType(String type) {
        this.type = type;
    }

    public static CanalEventType of(String type) {
        for (CanalEventType eventType : values()) {
            if (Objects.equals(eventType.type, type)) {
                return eventType;
            }
        }
        return null;
    }
}
