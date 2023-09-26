package com.wangtao.social.square.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@ToString
@Setter
@Getter
public class AddPostDTO {

    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    public String content;

    /**
     * 图片列表
     */
    public String[] imgUrlList;
}
