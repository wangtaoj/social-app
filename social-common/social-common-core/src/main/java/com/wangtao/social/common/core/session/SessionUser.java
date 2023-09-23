package com.wangtao.social.common.core.session;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangtao
 * Created at 2023-09-23
 */
@ToString
@Setter
@Getter
public class SessionUser {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 电话
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

}
