package com.wangtao.social.user.converter;

import com.wangtao.social.user.domain.SysUser;
import com.wangtao.social.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, typeConversionPolicy = ReportingPolicy.ERROR)
public interface UserConverter {

    UserDTO convertToDTO(SysUser sysUser);

    SysUser convert(UserDTO user);
}
