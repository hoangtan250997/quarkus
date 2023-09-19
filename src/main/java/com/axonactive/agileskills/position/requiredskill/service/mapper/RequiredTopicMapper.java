package com.axonactive.agileskills.position.requiredskill.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface RequiredTopicMapper extends BaseMapper<RequiredTopicEntity, RequiredTopic> {

    @Override
    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "requiredSkillId", source = "requiredSkill.id")
    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "topicDescription", source = "topic.description")
    RequiredTopic toDTO(RequiredTopicEntity entity);
}
