package com.wangtao.social.common.core.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
public class BaseModel implements Serializable {

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 修改时间
     */
    private LocalDate updateTime;

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
