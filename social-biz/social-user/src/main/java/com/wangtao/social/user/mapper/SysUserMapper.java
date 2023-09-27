package com.wangtao.social.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangtao.social.api.user.vo.UserVO;
import com.wangtao.social.user.po.SysUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023-09-17
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @MapKey("id")
    Map<Long, UserVO> selectByIds(@Param("userIds") Set<Long> userIds);

}




