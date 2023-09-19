package com.axonactive.agileskills.position.requiredskill.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta", uses = RequiredTopicMapper.class)
public interface RequiredSkillMapper extends BaseMapper<RequiredSkillEntity, RequiredSkill> {

    @Override
    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "positionId", source = "position.id")
    @Mapping(target = "skillName", source = "skill.name")
    @Mapping(target = "skillDescription", source = "skill.description")
    RequiredSkill toDTO(RequiredSkillEntity entity);
}
