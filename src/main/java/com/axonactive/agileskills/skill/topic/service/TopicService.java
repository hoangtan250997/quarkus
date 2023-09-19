package com.axonactive.agileskills.skill.topic.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.skill.dao.SkillDAO;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.topic.dao.TopicDAO;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import com.axonactive.agileskills.skill.topic.service.mapper.TopicMapper;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.collections4.CollectionUtils;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;


@ApplicationScoped
public class TopicService {

    @Inject
    private Validator validator;

    @Inject
    private TopicDAO topicDAO;

    @Inject
    private TopicMapper topicMapper;

    @Inject
    private SkillDAO skillDAO;

    public List<Topic> getBySkillIdAndStatus(Long skillId, StatusEnum status) throws ResourceNotFoundException {
        SkillEntity skill = skillDAO.findByIdAndStatus(skillId, StatusEnum.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.KEY_SKILL_NOT_FOUND,
                        ErrorMessage.SKILL_NOT_FOUND));
        return topicMapper.toDTOList(topicDAO.findBySkillIdAndStatus(skill.getId(), status));
    }

    public Topic create(Topic topic, Long skillId) throws ResourceNotFoundException, InputValidationException {
        verifyTopic(topic, skillId);
        TopicEntity createdTopic = TopicEntity.builder()
                .name(topic.getName().trim())
                .description(topic.getDescription() == null ? null : topic.getDescription().trim())
                .status(StatusEnum.ACTIVE)
                .skill(skillDAO.findByIdAndStatus(skillId, StatusEnum.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.KEY_SKILL_NOT_FOUND,
                                ErrorMessage.SKILL_NOT_FOUND)))
                .build();

        return topicMapper.toDTO(topicDAO.create(createdTopic));

    }

    public Topic softDelete(Long id) throws ResourceNotFoundException {
        TopicEntity deletedTopic = topicDAO.softDelete(id);
        if (deletedTopic == null) {
            throw new ResourceNotFoundException(ErrorMessage.KEY_TOPIC_NOT_FOUND, ErrorMessage.TOPIC_NOT_FOUND);
        }
        return topicMapper.toDTO(deletedTopic);
    }

    private void verifyTopic(Topic topic, Long skillId) throws ResourceNotFoundException, InputValidationException {
        if (topic.getName() != null) {
            topic.setName(topic.getName().trim());
        }
        Set<ConstraintViolation<Topic>> violations = validator.validate(topic);
        if (CollectionUtils.isNotEmpty(violations)) {
            throw new ConstraintViolationException(violations);
        }
        if (isSkillNotExist(skillId)) {
            throw new ResourceNotFoundException(ErrorMessage.KEY_SKILL_NOT_FOUND, ErrorMessage.SKILL_NOT_FOUND);
        }
        if (isNameDuplicated(topic, skillId)) {
            throw new InputValidationException(ErrorMessage.KEY_DUPLICATED_TOPIC_NAME,
                    ErrorMessage.DUPLICATED_TOPIC_NAME);
        }
    }

    private boolean isNameDuplicated(Topic topic, Long skillId) {
        return topicDAO.findBySkillIdAndStatus(skillId, StatusEnum.ACTIVE).stream()
                .anyMatch(topicEntity -> topicEntity.getName().equalsIgnoreCase(topic.getName().trim()));
    }

    private boolean isSkillNotExist(Long skillId) {
        return skillDAO.findByIdAndStatus(skillId, StatusEnum.ACTIVE).isEmpty();
    }
}
