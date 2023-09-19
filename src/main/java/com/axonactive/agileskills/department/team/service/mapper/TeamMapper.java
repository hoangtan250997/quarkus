package com.axonactive.agileskills.department.team.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.department.team.service.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "jakarta", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeamMapper extends BaseMapper<TeamEntity, Team> {

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Override
    Team toDTO(TeamEntity entity);
}
