package com.axonactive.agileskills.position.service;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.team.dao.TeamDAO;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.position.dao.PositionDAO;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import com.axonactive.agileskills.position.requiredskill.dao.RequiredTopicDAO;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import com.axonactive.agileskills.position.requiredskill.service.RequiredSkillService;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredTopic;
import com.axonactive.agileskills.position.service.mapper.PositionMapper;
import com.axonactive.agileskills.position.service.model.Position;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.axonactive.agileskills.base.entity.StatusEnum.ACTIVE;
import static com.axonactive.agileskills.base.entity.StatusEnum.INACTIVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.DUPLICATED_POSITION_NAME;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_DUPLICATED_POSITION_NAME;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TEAM_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.POSITION_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TEAM_NOT_FOUND;
import static com.axonactive.agileskills.position.entity.PositionStatusEnum.OPEN;

@ApplicationScoped
public class PositionService {

    private static final ZoneId serverZone = TimeZone.getDefault().toZoneId();

    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Inject
    private PositionDAO positionDAO;

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private PositionMapper positionMapper;

    @Inject
    private RequiredSkillService requiredSkillService;

    @Inject
    private SearchPositionCache searchPositionCache;

    @Inject
    private PositionListCache positionListCache;

    @Inject
    private RequiredTopicDAO requiredTopicDAO;

    private void filterInactiveSkillAndTopic(PositionEntity positionEntity) {
        Predicate<RequiredSkillEntity> inactiveSkill = rs -> (INACTIVE).equals(rs.getSkill().getStatus());
        Predicate<RequiredTopicEntity> inactiveTopic = rt -> (INACTIVE).equals(rt.getTopic().getStatus());
        if (positionEntity.getRequiredSkillList() != null) {
            positionEntity.getRequiredSkillList().stream()
                    .filter(inactiveSkill)
                    .collect(Collectors.toList());

            for (RequiredSkillEntity requiredSkillEntity : positionEntity.getRequiredSkillList()) {
                requiredSkillEntity.getRequiredTopicList().stream()
                        .filter(inactiveTopic)
                        .collect(Collectors.toList());
            }
        }
    }

    @Transactional
    public PositionEntity displayPositionWithRequiredSkillAndRequiredTopic(Long id) throws ResourceNotFoundException {
        PositionEntity positionEntity = positionDAO.findByIdAndStatus(id, OPEN)
                .orElseThrow(() -> new ResourceNotFoundException(KEY_POSITION_NOT_FOUND, POSITION_NOT_FOUND));

        filterInactiveSkillAndTopic(positionEntity);

        List<RequiredSkillEntity> filteredSkills = filterRequiredSkillList(positionEntity);

        positionEntity.setRequiredSkillList(filteredSkills);

        return positionEntity;
    }

    public List<Position> getByStatus(PositionStatusEnum status) {
        List<PositionEntity> positionCacheResult = positionListCache.getCache().getIfPresent(status.name());

        if (positionCacheResult != null) {
            return positionMapper.toDTOList(positionCacheResult);
        }

        List<PositionEntity> positionEntityList = positionDAO.findByStatus(status);
        for (PositionEntity positionEntity : positionEntityList) {
            filterInactiveSkillAndTopic(positionEntity);
        }
        positionListCache.getCache().put(status.name(), positionEntityList);

        return positionMapper.toDTOList(positionEntityList);
    }

    public void close(Long id) throws ResourceNotFoundException {
        PositionEntity closedPosition = positionDAO.close(id);

        if (closedPosition == null) {
            throw new ResourceNotFoundException(KEY_POSITION_NOT_FOUND, POSITION_NOT_FOUND);
        }

        invalidateCache();
    }

    public Position getByIdAndStatus(Long id, PositionStatusEnum status) throws ResourceNotFoundException {
        PositionEntity positionEntity = positionDAO.findByIdAndStatus(id, status)
                .orElseThrow(() -> new ResourceNotFoundException(KEY_POSITION_NOT_FOUND, POSITION_NOT_FOUND));

        return positionMapper.toDTO(positionEntity);
    }

    public List<Integer> getYears() {
        return positionDAO.findYears();
    }

    public List<PositionEntity> getByYear(Integer year) {

        return positionDAO.findByYear(year);
    }

    @Transactional
    public Position createPositionWithRequiredSkill(Position position) throws InputValidationException, ResourceNotFoundException {
        verifyPosition(position);
        checkForDuplicatedPositionName(position);

        TeamEntity team = getTeamById(position.getTeamId());
        PositionEntity newPosition = createNewPosition(position, team);

        if (position.getRequiredSkillList() != null) {
            List<RequiredSkillEntity> requiredSkillList = requiredSkillService.createEntityList(newPosition, position);
            newPosition.setRequiredSkillList(requiredSkillList);
        }

        PositionEntity newPositionEntity = positionDAO.create(newPosition);

        invalidateCache();

        return positionMapper.toDTO(newPositionEntity);
    }

    public PositionEntity update(Long id, Position position) throws ResourceNotFoundException, InputValidationException {
        verifyPosition(position);
        checkForDuplicatedPositionNameForUpdate(id, position);
        PositionEntity updatedPosition = getOpenPositionById(id);
        TeamEntity team = getTeamById(position.getTeamId());

        updatePositionEntity(position, updatedPosition, team);

        if (position.getRequiredSkillList() != null) {
            if (CollectionUtils.isNotEmpty(updatedPosition.getRequiredSkillList())) {
                requiredSkillService.deleteRequiredSkillAndTopicList(updatedPosition);
            }
            List<RequiredSkillEntity> requiredSkillEntityList = requiredSkillService.createEntityList(updatedPosition, position);
            updatedPosition.setRequiredSkillList(requiredSkillEntityList);
        } else {
            updatedPosition.setRequiredSkillList(new ArrayList<>());
        }

        invalidateCache();

        return positionDAO.update(updatedPosition);
    }

    private void updatePositionEntity(Position position, PositionEntity updatedPosition, TeamEntity team) {
        updatedPosition.setTeam(team);
        updatedPosition.setName(position.getName());
        updatedPosition.setNote(position.getNote());
        updatedPosition.setQuantity(position.getQuantity());
        updatedPosition.setOpenedDate(position.getOpenedDate() == null ? LocalDateTime.now() : convertToLocalDateTime(position.getOpenedDate()));
    }

    private PositionEntity getOpenPositionById(Long id) throws ResourceNotFoundException {
        return positionDAO.findByIdAndStatus(id, OPEN)
                .orElseThrow(() -> new ResourceNotFoundException(KEY_POSITION_NOT_FOUND, POSITION_NOT_FOUND));
    }

    public List<Position> searchOpenPositionsByWord(String word) {
        if (word.contains("!")) {
            word = word.replace("!", "#");
        } else if (word.contains("@")) {
            word = word.replace("@", "+");
        }
        String input = word.trim().toLowerCase();
        List<PositionEntity> cachedResult = searchPositionCache.getCache().getIfPresent(input);

        if (cachedResult != null) {
            return positionMapper.toDTOList(cachedResult);
        }

        List<PositionEntity> positionList = positionDAO.searchStatus(word, OPEN);

        if (CollectionUtils.isNotEmpty(positionList)) {
            searchPositionCache.getCache().put(input, positionList);
        }
        for (PositionEntity positionEntity : positionList) {
            filterInactiveSkillAndTopic(positionEntity);
        }

        return positionMapper.toDTOList(positionList);

    }

    private void verifyPosition(Position position) {
        trimPositionName(position);
        validatePositionConstraints(position);
        validateRequiredSkillsAndTopics(position);
    }

    private TeamEntity getTeamById(Long teamId) throws ResourceNotFoundException {
        return teamDAO.findByIdAndStatus(teamId, ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(KEY_TEAM_NOT_FOUND, TEAM_NOT_FOUND));
    }

    private void trimPositionName(Position position) {
        if (position.getName() != null) {
            position.setName(position.getName().trim());
        }
    }

    private void validatePositionConstraints(Position position) throws ConstraintViolationException {
        Set<ConstraintViolation<Position>> positionViolationSet = validator.validate(position);
        if (CollectionUtils.isNotEmpty(positionViolationSet)) {
            throw new ConstraintViolationException(positionViolationSet);
        }
    }

    private void validateRequiredSkillsAndTopics(Position position) throws ConstraintViolationException {
        if (position.getRequiredSkillList() != null) {
            for (RequiredSkill requiredSkill : position.getRequiredSkillList()) {
                validateSkill(requiredSkill);

                if (requiredSkill.getRequiredTopicList() != null) {
                    for (RequiredTopic requiredTopic : requiredSkill.getRequiredTopicList()) {
                        validateTopic(requiredTopic);
                    }
                }
            }
        }
    }

    private void validateSkill(RequiredSkill requiredSkill) throws ConstraintViolationException {
        Set<ConstraintViolation<RequiredSkill>> skillViolationSet = validator.validate(requiredSkill);
        if (!skillViolationSet.isEmpty()) {
            throw new ConstraintViolationException(skillViolationSet);
        }
    }

    private void validateTopic(RequiredTopic requiredTopic) throws ConstraintViolationException {
        Set<ConstraintViolation<RequiredTopic>> topicViolationSet = validator.validate(requiredTopic);
        if (!topicViolationSet.isEmpty()) {
            throw new ConstraintViolationException(topicViolationSet);
        }
    }

    private void checkForDuplicatedPositionName(Position position) throws InputValidationException {
        if (isPositionNameExisted(position)) {
            throw new InputValidationException(KEY_DUPLICATED_POSITION_NAME, DUPLICATED_POSITION_NAME);
        }
    }

    private void checkForDuplicatedPositionNameForUpdate(Long id, Position position) throws ResourceNotFoundException,
            InputValidationException {
        PositionEntity positionEntity = getOpenPositionById(id);
        if (position.getName() != null &&
                (!positionEntity.getName().equalsIgnoreCase(position.getName()) || !positionEntity.getTeam().getId().equals(position.getTeamId()))) {
            checkForDuplicatedPositionName(position);
        }
    }

    private boolean isPositionNameExisted(Position position) {
        return positionDAO.findByTeamIdAndStatus(position.getTeamId(), OPEN).stream()
                .anyMatch(positionEntity -> positionEntity.getName().equalsIgnoreCase(position.getName().trim()));
    }

    public PositionEntity createNewPosition(Position position, TeamEntity team) {
        return PositionEntity.builder()
                .name(position.getName().trim())
                .note(position.getNote() == null ? null : position.getNote().trim())
                .quantity(position.getQuantity())
                .status(OPEN)
                .team(team)
                .createdDate(LocalDateTime.now())
                .openedDate(position.getOpenedDate() == null ? LocalDateTime.now() : convertToLocalDateTime(position.getOpenedDate()))
                .build();
    }

    private LocalDateTime convertToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(serverZone).toLocalDateTime();
    }

    private List<RequiredSkillEntity> filterRequiredSkillList(PositionEntity positionEntity) {
        return positionEntity.getRequiredSkillList().stream()
                .filter(Objects::nonNull)
                .map(this::filterRequiredSkill)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private RequiredSkillEntity filterRequiredSkill(RequiredSkillEntity rqSkill) {
        List<RequiredTopicEntity> filteredTopics = rqSkill.getRequiredTopicList().stream()
                .filter(Objects::nonNull)
                .filter(rqTopic ->
                        (rqTopic.getLevel() != null && rqTopic.getRequire() != null) ||
                                (rqTopic.getNote() != null && !rqTopic.getNote().isEmpty()))
                .collect(Collectors.toList());

        if ((rqSkill.getLevel() != null && rqSkill.getRequire() != null)
                || (rqSkill.getNote() != null && !rqSkill.getNote().isEmpty())
                || !filteredTopics.isEmpty()) {
            rqSkill.setRequiredTopicList(filteredTopics);
            return rqSkill;
        }
        return null;
    }

    private void invalidateCache() {
        positionListCache.getCache().invalidateAll();
        searchPositionCache.getCache().invalidateAll();
    }
}
