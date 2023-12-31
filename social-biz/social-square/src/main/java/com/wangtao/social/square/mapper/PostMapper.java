package com.wangtao.social.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangtao.social.square.api.dto.PostQueryDTO;
import com.wangtao.social.square.api.vo.PostVO;
import com.wangtao.social.square.po.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 根据点赞数量排序
     * @param postQuery 查询参数
     * @return 帖子列表
     */
    IPage<PostVO> list(IPage<PostVO> page, @Param("postQuery") PostQueryDTO postQuery);

    /**
     * 统计用户下面所有帖子的点赞数量
     * @param userId 用户id
     * @return 所有帖子的点赞数量
     */
    long selectLikeCountByUserId(@Param("userId") Long userId);
}




