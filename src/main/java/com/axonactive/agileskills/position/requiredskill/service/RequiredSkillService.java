package com.axonactive.agileskills.position.requiredskill.service;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.dao.RequiredSkillDAO;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.axonactive.agileskills.position.service.model.Position;
import com.axonactive.agileskills.skill.dao.SkillDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validator;
import org.apache.commons.collections4.CollectionUtils;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.axonactive.agileskills.base.entity.StatusEnum.ACTIVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_REQUIRED_SKILL_LEVEL_MUST_BE_MASTER;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_ID_DUPLICATED;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_LEVEL_MUST_BE_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.REQUIRED_SKILL_LEVEL_MUST_BE_MASTER;
import static com.axonactive.agileskills.base.exception.ErrorMessage.REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_ID_DUPLICATED;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_LEVEL_MUST_BE_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.MASTER;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.MUST_HAVE;

@ApplicationScoped
public class RequiredSkillService {

    @Inject
    private SkillDAO skillDAO;

    @Inject
    private RequiredTopicService requiredTopicService;

    @Inject
    private RequiredSkillDAO requiredSkillDAO;

    public List<RequiredSkillEntity> createEntityList(PositionEntity positionEntity, Position position) throws ResourceNotFoundException, InputValidationException {
        verifyDuplicatedSkillId(position);

        List<RequiredSkillEntity> requiredSkillEntityList = new ArrayList<>();

        for (RequiredSkill requiredSkill : position.getRequiredSkillList()) {

            validateRequiredSkill(requiredSkill);

            if (requiredSkill.getRequiredTopicList() != null) {
                checkSkillLevelWithTopicLevel(requiredSkill);
            }

            RequiredSkillEntity requiredSkillEntity = createEntity(positionEntity, requiredSkill);
            requiredSkillEntityList.add(requiredSkillEntity);
        }

        return requiredSkillEntityList;
    }

    public void deleteRequiredSkillAndTopicList(PositionEntity updatedPosition) {
        requiredTopicService.deleteOldRequiredTopicList(updatedPosition);
        deleteOldRequiredSkillList(updatedPosition);
    }

    private void deleteOldRequiredSkillList(PositionEntity updatedPosition) {
        if (CollectionUtils.isNotEmpty(updatedPosition.getRequiredSkillList())) {
            List<Long> requiredSkillIdList = updatedPosition.getRequiredSkillList().stream()
                    .map(RequiredSkillEntity::getId)
                    .collect(Collectors.toList());
            requiredSkillDAO.deleteInList(requiredSkillIdList);
        }
    }

    private RequiredSkillEntity createEntity(PositionEntity positionEntity, RequiredSkill requiredSkill) throws ResourceNotFoundException, InputValidationException, ConstraintViolationException {
        RequiredSkillEntity requiredSkillEntity = RequiredSkillEntity.builder()
                .skill(skillDAO.findByIdAndStatus(requiredSkill.getSkillId(), ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException(KEY_SKILL_NOT_FOUND, SKILL_NOT_FOUND)))
                .position(positionEntity)
                .note(requiredSkill.getNote() == null ? null : requiredSkill.getNote().trim())
                .require(requiredSkill.getRequire())
                .level(requiredSkill.getLevel())
                .build();

        if (requiredSkill.getRequiredTopicList() != null) {
            requiredSkillEntity.setRequiredTopicList(requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill));
        }

        return requiredSkillEntity;
    }

    private void verifyDuplicatedSkillId(Position position) throws InputValidationException {
        Set<Long> skillIdSet = new HashSet<>();

        for (RequiredSkill requiredSkill : position.getRequiredSkillList()) {
            boolean isAdded = skillIdSet.add(requiredSkill.getSkillId());

            if (!isAdded) {
                throw new InputValidationException(KEY_SKILL_ID_DUPLICATED, SKILL_ID_DUPLICATED);
            }
        }
    }

    private void validateRequiredSkill(RequiredSkill requiredSkill) throws InputValidationException, ConstraintViolationException {
        if (requiredSkill.getRequire() != null) {
            if (requiredSkill.getLevel() == null) {
                throw new InputValidationException(KEY_SKILL_LEVEL_MUST_BE_GREATER_THAN_0, SKILL_LEVEL_MUST_BE_GREATER_THAN_0);
            }
        } else if (requiredSkill.getLevel() != null) {
            throw new InputValidationException(KEY_SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE, SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE);
        }
    }

    private void checkSkillLevelWithTopicLevel(RequiredSkill requiredSkill) throws InputValidationException {
        boolean hasRequiredTopicWithLevelWithoutSkillLevel = requiredSkill.getLevel() == null &&
                requiredSkill.getRequiredTopicList().stream()
                        .anyMatch(requiredTopic -> requiredTopic.getLevel() != null);

        boolean hasRequiredTopicWithMustHaveWithoutSkillMustHave = requiredSkill.getRequire() != null &&
                !requiredSkill.getRequire().equals(MUST_HAVE) &&
                requiredSkill.getRequiredTopicList().stream()
                        .filter(requiredTopic -> requiredTopic.getRequire() != null)
                        .anyMatch(requiredTopic -> requiredTopic.getRequire().equals(MUST_HAVE));

        boolean hasRequiredTopicWithMasterWithoutSkillMaster = requiredSkill.getLevel() != null &&
                !requiredSkill.getLevel().equals(MASTER) &&
                requiredSkill.getRequiredTopicList().stream()
                        .filter(requiredTopic -> requiredTopic.getLevel() != null)
                        .anyMatch(requiredTopic -> requiredTopic.getLevel().equals(MASTER));

        if (hasRequiredTopicWithLevelWithoutSkillLevel) {
            throw new InputValidationException(KEY_SKILL_LEVEL_MUST_BE_GREATER_THAN_0, SKILL_LEVEL_MUST_BE_GREATER_THAN_0);
        }

        if (hasRequiredTopicWithMustHaveWithoutSkillMustHave) {
            throw new InputValidationException(KEY_REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE, REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE);
        }

        if (hasRequiredTopicWithMasterWithoutSkillMaster) {
            throw new InputValidationException(KEY_REQUIRED_SKILL_LEVEL_MUST_BE_MASTER, REQUIRED_SKILL_LEVEL_MUST_BE_MASTER);
        }
    }
}
