package com.wangtao.social.user.converter;

import com.wangtao.social.common.user.dto.UserMessageDTO;
import com.wangtao.social.user.po.UserInbox;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author wangtao
 * Created at 2023-10-18
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, typeConversionPolicy = ReportingPolicy.ERROR)
public interface UserInboxConverter {

    UserInbox convert(UserMessageDTO userMessage);
}
