package com.axonactive.agileskills.skill.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.service.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "jakarta", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SkillMapper extends BaseMapper<SkillEntity, Skill> {
}
