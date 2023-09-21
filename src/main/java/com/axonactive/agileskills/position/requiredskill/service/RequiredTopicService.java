package com.axonactive.agileskills.position.requiredskill.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.dao.RequiredTopicDAO;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredTopic;
import com.axonactive.agileskills.skill.topic.dao.TopicDAO;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_ID_DUPLICATED;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_IS_NOT_BELONG_TO_THIS_SKILL;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_LEVEL_MUST_BE_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_ID_DUPLICATED;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_IS_NOT_BELONG_TO_THIS_SKILL;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_LEVEL_MUST_BE_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE;

@ApplicationScoped
public class RequiredTopicService {

    @Inject
    private Validator validator;

    @Inject
    private TopicDAO topicDAO;

    @Inject
    private RequiredTopicDAO requiredTopicDAO;

    public List<RequiredTopicEntity> createEntityList(RequiredSkillEntity requiredSkillEntity, RequiredSkill requiredSkill) throws ResourceNotFoundException, InputValidationException {
        verifyDuplicatedTopicId(requiredSkill);

        List<RequiredTopicEntity> requiredTopicEntityList = new ArrayList<>();

        for (RequiredTopic requiredTopic : requiredSkill.getRequiredTopicList()) {
            TopicEntity topicEntity = topicDAO.findByIdAndStatus(requiredTopic.getTopicId(), StatusEnum.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException(KEY_TOPIC_NOT_FOUND, TOPIC_NOT_FOUND));

            validateRequiredTopic(requiredTopic);
            checkRequiredTopicConstraint(requiredSkillEntity, topicEntity);

            RequiredTopicEntity requiredTopicEntity = createEntity(requiredSkillEntity, requiredTopic, topicEntity);
            requiredTopicEntityList.add(requiredTopicEntity);
        }

        return requiredTopicEntityList;
    }

    public void deleteOldRequiredTopicList(PositionEntity updatedPosition) {
        updatedPosition.getRequiredSkillList().forEach(requiredSkill -> {
            if (CollectionUtils.isNotEmpty(requiredSkill.getRequiredTopicList())) {
                List<RequiredTopicEntity> requiredTopicList = requiredSkill.getRequiredTopicList();
                List<Long> requiredTopicIdList = requiredTopicList.stream()
                        .map(RequiredTopicEntity::getId)
                        .collect(Collectors.toList());
                requiredTopicDAO.deleteInList(requiredTopicIdList);
            }
        });
    }

    private void verifyDuplicatedTopicId(RequiredSkill requiredSkill) throws InputValidationException {
        Set<Long> topicIdSet = new HashSet<>();

        for (RequiredTopic requiredTopic : requiredSkill.getRequiredTopicList()) {
            boolean isAdded = topicIdSet.add(requiredTopic.getTopicId());

            if (!isAdded) {
                throw new InputValidationException(KEY_TOPIC_ID_DUPLICATED, TOPIC_ID_DUPLICATED);
            }
        }
    }

    private RequiredTopicEntity createEntity(RequiredSkillEntity requiredSkillEntity, RequiredTopic requiredTopic, TopicEntity topicEntity) {
        return RequiredTopicEntity.builder()
                .topic(topicEntity)
                .requiredSkill(requiredSkillEntity)
                .require(requiredTopic.getRequire())
                .level(requiredTopic.getLevel())
                .note(requiredTopic.getNote() == null ? null : requiredTopic.getNote().trim())
                .build();
    }

    private void checkRequiredTopicConstraint(RequiredSkillEntity requiredSkillEntity, TopicEntity topicEntity) throws InputValidationException {
        if (!topicEntity.getSkill().getId().equals(requiredSkillEntity.getSkill().getId())) {
            throw new InputValidationException(KEY_TOPIC_IS_NOT_BELONG_TO_THIS_SKILL, TOPIC_IS_NOT_BELONG_TO_THIS_SKILL);
        }
    }

    private void validateRequiredTopic(RequiredTopic requiredTopic) throws InputValidationException {
        if (requiredTopic.getRequire() != null) {
            if (requiredTopic.getLevel() == null) {
                throw new InputValidationException(KEY_TOPIC_LEVEL_MUST_BE_GREATER_THAN_0, TOPIC_LEVEL_MUST_BE_GREATER_THAN_0);
            }
        } else if (requiredTopic.getLevel() != null) {
            throw new InputValidationException(KEY_TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE, TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE);
        }
    }
}
