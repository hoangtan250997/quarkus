package com.axonactive.agileskills.position.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.service.mapper.RequiredSkillMapper;
import com.axonactive.agileskills.position.service.model.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "jakarta", uses = RequiredSkillMapper.class)
public interface PositionMapper extends BaseMapper<PositionEntity, Position> {

    @Override
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    @Mapping(target = "departmentName", source = "team.department.name")
    @Mapping(target = "createdDate", qualifiedByName = "mapToOffsetDateTime")
    @Mapping(target = "openedDate", qualifiedByName = "mapToOffsetDateTime")
    @Mapping(target = "closedDate", qualifiedByName = "mapToOffsetDateTime")
    Position toDTO(PositionEntity entity);

    @Named("mapToOffsetDateTime")
    default OffsetDateTime mapToOffsetDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        ZoneOffset offset = ZoneOffset.of("+07:00");

        return localDateTime.atOffset(offset);
    }
}
