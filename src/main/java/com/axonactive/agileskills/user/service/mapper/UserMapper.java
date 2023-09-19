package com.axonactive.agileskills.user.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.user.entity.UserEntity;
import com.axonactive.agileskills.user.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "jakarta", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<UserEntity, User> {

    @Override
    @Mapping(target = "password", ignore = true)
    User toDTO(UserEntity entity);
}
