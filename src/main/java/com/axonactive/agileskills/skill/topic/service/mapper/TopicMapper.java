package com.axonactive.agileskills.skill.topic.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "jakarta", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TopicMapper extends BaseMapper<TopicEntity, Topic> {

    @Override
    @Mapping(target = "skillId", source = "skill.id")
    Topic toDTO(TopicEntity topic);
}
