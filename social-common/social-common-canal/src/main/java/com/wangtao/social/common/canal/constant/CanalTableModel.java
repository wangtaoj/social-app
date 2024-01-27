package com.wangtao.social.common.canal.constant;

import lombok.Getter;

import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023/4/1 15:33
 */
@Getter
public enum CanalTableModel {

    SS_POST("social", "ss_post"),

    UN_KNOWN("UN_KNOWN", "UN_KNOWN"),
    ;

    private final String schema;

    private final String table;

    CanalTableModel(String schema, String table) {
        Objects.requireNonNull(schema);
        Objects.requireNonNull(table);
        this.schema = schema;
        this.table = table;
    }

    public static CanalTableModel of(String schema, String table) {
        for (CanalTableModel canalTableModel : values()) {
            if (Objects.equals(canalTableModel.schema, schema) && Objects.equals(canalTableModel.table, table)) {
                return canalTableModel;
            }
        }
        return UN_KNOWN;
    }
}
