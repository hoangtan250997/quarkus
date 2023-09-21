package com.axonactive.agileskills.position.requiredskill.service;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.dao.RequiredTopicDAO;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredTopic;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.topic.dao.TopicDAO;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.axonactive.agileskills.base.entity.StatusEnum.ACTIVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_ID_DUPLICATED;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_IS_NOT_BELONG_TO_THIS_SKILL;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_LEVEL_MUST_BE_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE;
import static com.axonactive.agileskills.position.entity.PositionStatusEnum.OPEN;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.MASTER;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.MUST_HAVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class RequiredTopicServiceTest {

    @InjectMocks
    private RequiredTopicService requiredTopicService;

    @Mock
    private TopicDAO topicDAO;

    @Mock
    private RequiredTopicDAO requiredTopicDAO;

    @Test
    void deleteOldRequiredTopicList_requiredTopicListIsEmpty_keepOldRequiredTopicList() {
        PositionEntity updatedPositionEntity = getPositionEntity();
        List<RequiredTopicEntity> requiredTopicEntityList = new ArrayList<>();
        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        requiredSkillEntity.setRequiredTopicList(requiredTopicEntityList);
        updatedPositionEntity.setRequiredSkillList(List.of(requiredSkillEntity));

        requiredTopicService.deleteOldRequiredTopicList(updatedPositionEntity);

        verifyNoInteractions(requiredTopicDAO);
    }

    @Test
    void deleteOldRequiredTopicList_requiredTopicListIsNotEmpty_deleteOldRequiredTopicList() {
        PositionEntity updatedPositionEntity = getPositionEntity();
        RequiredTopicEntity requiredTopicEntity1 = getRequiredTopicEntity1();
        RequiredTopicEntity requiredTopicEntity2 = getRequiredTopicEntity2();

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        requiredSkillEntity.setRequiredTopicList(Arrays.asList(requiredTopicEntity1, requiredTopicEntity2));
        updatedPositionEntity.setRequiredSkillList(List.of(requiredSkillEntity));

        requiredTopicService.deleteOldRequiredTopicList(updatedPositionEntity);

        verify(requiredTopicDAO).deleteInList(anyList());
    }

    @Test
    void createEntityList_topicIdDuplicated_throwException() {
        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        RequiredSkill requiredSkill = getRequiredSkill();
        List<RequiredTopic> duplicatedRequiredTopicList = getDuplicatedRequiredTopicList();
        requiredSkill.setRequiredTopicList(duplicatedRequiredTopicList);

        assertThrows(InputValidationException.class,
                () -> requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill), TOPIC_ID_DUPLICATED);
    }

    @Test
    void createEntityList_TopicNotFound_ThrowException() {
        when(topicDAO.findByIdAndStatus(999L, ACTIVE)).thenReturn(Optional.empty());

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        RequiredSkill requiredSkill = getRequiredSkill();
        RequiredTopic requiredTopic = getTopicNotFoundRequiredTopic();
        requiredSkill.setRequiredTopicList(Collections.singletonList(requiredTopic));

        assertThrows(ResourceNotFoundException.class,
                () -> requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill), TOPIC_NOT_FOUND);
    }

    @Test
    void createEntityList_InvalidRequireAndLevel_ThrowException() {
        TopicEntity topicEntity = getTopicEntity1();

        when(topicDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(topicEntity));

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        RequiredSkill requiredSkill = getRequiredSkill();
        RequiredTopic requiredTopic1 = getInvalidRequireAndLevelRequiredTopic1();
        RequiredTopic requiredTopic2 = getInvalidRequireAndLevelRequiredTopic2();
        requiredSkill.setRequiredTopicList(Collections.singletonList(requiredTopic1));

        assertThrows(InputValidationException.class,
                () -> requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill), TOPIC_LEVEL_MUST_BE_GREATER_THAN_0);

        requiredSkill.setRequiredTopicList(Collections.singletonList(requiredTopic2));

        assertThrows(InputValidationException.class,
                () -> requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill), TOPIC_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE);
    }

    @Test
    void createEntityList_RequiredTopicNotConstraint_ThrowException() {
        TopicEntity topicEntity = getTopicEntity1();

        when(topicDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(topicEntity));

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity2();
        RequiredSkill requiredSkill = getRequiredSkill();
        List<RequiredTopic> requiredTopicList = Arrays.asList(getRequiredTopic1(), getRequiredTopic2());
        requiredSkill.setRequiredTopicList(requiredTopicList);

        assertThrows(InputValidationException.class,
                () -> requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill), TOPIC_IS_NOT_BELONG_TO_THIS_SKILL);
    }

    @Test
    void createEntityList_ValidInformation_CreateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        TopicEntity topicEntity = getTopicEntity1();

        when(topicDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(topicEntity));

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        RequiredSkill requiredSkill = getRequiredSkill();
        RequiredTopic requiredTopic1 = getRequiredTopic1();
        List<RequiredTopic> requiredTopicList = Collections.singletonList(requiredTopic1);
        requiredSkill.setRequiredTopicList(requiredTopicList);

        List<RequiredTopicEntity> requiredTopicEntityList = requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill);

        assertEquals(requiredTopicList.size(), requiredTopicEntityList.size());
        assertEquals(requiredTopic1.getId(), requiredTopicEntityList.get(0).getId());
        assertEquals(requiredTopic1.getTopicId(), requiredTopicList.get(0).getTopicId());
        assertEquals(requiredTopic1.getRequire(), requiredTopicList.get(0).getRequire());
        assertEquals(requiredTopic1.getRequiredSkillId(), requiredTopicList.get(0).getRequiredSkillId());
        assertEquals(requiredTopic1.getLevel(), requiredTopicList.get(0).getLevel());
    }

    @Test
    void createEntityList_noteIsNull_CreateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        TopicEntity topicEntity = getTopicEntity1();

        when(topicDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(topicEntity));

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        RequiredSkill requiredSkill = getRequiredSkill();

        RequiredTopic requiredTopic1 = getRequiredTopic1();
        requiredTopic1.setNote(null);

        List<RequiredTopic> requiredTopicList = Collections.singletonList(requiredTopic1);
        requiredSkill.setRequiredTopicList(requiredTopicList);

        List<RequiredTopicEntity> requiredTopicEntityList = requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill);

        assertEquals(requiredTopicList.size(), requiredTopicEntityList.size());
        assertEquals(requiredTopic1.getId(), requiredTopicEntityList.get(0).getId());
        assertEquals(requiredTopic1.getTopicId(), requiredTopicList.get(0).getTopicId());
        assertEquals(requiredTopic1.getRequire(), requiredTopicList.get(0).getRequire());
        assertEquals(requiredTopic1.getRequiredSkillId(), requiredTopicList.get(0).getRequiredSkillId());
        assertEquals(requiredTopic1.getLevel(), requiredTopicList.get(0).getLevel());
    }

    @Test
    void createEntityList_NoteLengthIs2000Chars_CreateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        TopicEntity topicEntity = getTopicEntity1();

        when(topicDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(topicEntity));

        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        RequiredSkill requiredSkill = getRequiredSkill();

        RequiredTopic requiredTopic1 = getRequiredTopic1();
        requiredTopic1.setNote("a".repeat(2000));

        List<RequiredTopic> requiredTopicList = Collections.singletonList(requiredTopic1);
        requiredSkill.setRequiredTopicList(requiredTopicList);

        List<RequiredTopicEntity> requiredTopicEntityList = requiredTopicService.createEntityList(requiredSkillEntity, requiredSkill);

        assertEquals(requiredTopicList.size(), requiredTopicEntityList.size());
        assertEquals(requiredTopic1.getId(), requiredTopicEntityList.get(0).getId());
        assertEquals(requiredTopic1.getTopicId(), requiredTopicList.get(0).getTopicId());
        assertEquals(requiredTopic1.getRequire(), requiredTopicList.get(0).getRequire());
        assertEquals(requiredTopic1.getRequiredSkillId(), requiredTopicList.get(0).getRequiredSkillId());
        assertEquals(requiredTopic1.getLevel(), requiredTopicList.get(0).getLevel());
    }

    private RequiredTopic getInvalidRequireAndLevelRequiredTopic1() {
        return RequiredTopic.builder()
                .id(1L)
                .topicId(1L)
                .level(null)
                .require(MUST_HAVE)
                .requiredSkillId(1L)
                .build();
    }

    private RequiredTopic getInvalidRequireAndLevelRequiredTopic2() {
        return RequiredTopic.builder()
                .id(1L)
                .topicId(1L)
                .level(MASTER)
                .require(null)
                .requiredSkillId(1L)
                .build();
    }

    private RequiredTopic getTopicNotFoundRequiredTopic() {
        return RequiredTopic.builder()
                .id(1L)
                .topicId(999L)
                .level(MASTER)
                .require(MUST_HAVE)
                .requiredSkillId(1L)
                .build();
    }

    private RequiredTopic getRequiredTopic1() {
        return RequiredTopic.builder()
                .topicId(1L)
                .build();
    }

    private RequiredTopic getRequiredTopic2() {
        return RequiredTopic.builder()
                .topicId(2L)
                .build();
    }

    private RequiredTopicEntity getRequiredTopicEntity1() {
        return RequiredTopicEntity.builder()
                .id(1L)
                .topic(getTopicEntity1())
                .require(MUST_HAVE)
                .level(MASTER)
                .build();
    }

    private RequiredTopicEntity getRequiredTopicEntity2() {
        return RequiredTopicEntity.builder()
                .id(2L)
                .topic(getTopicEntity2())
                .require(MUST_HAVE)
                .level(MASTER)
                .build();
    }

    private SkillEntity getSkillEntity() {
        return SkillEntity.builder()
                .id(1L)
                .name("Sample Skill")
                .status(ACTIVE)
                .build();
    }

    private SkillEntity getSkillEntity2() {
        return SkillEntity.builder()
                .id(2L)
                .name("Sample Skill 2")
                .status(ACTIVE)
                .build();
    }

    private TopicEntity getTopicEntity1() {
        return TopicEntity.builder()
                .id(1L)
                .name("Sample Topic 1")
                .description("A sample topic 1")
                .status(ACTIVE)
                .skill(getSkillEntity())
                .build();
    }

    private TopicEntity getTopicEntity2() {
        return TopicEntity.builder()
                .id(2L)
                .name("Sample Topic 2")
                .description("A sample topic 2")
                .status(ACTIVE)
                .build();
    }

    private PositionEntity getPositionEntity() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample Position")
                .quantity(10)
                .status(OPEN)
                .team(getTeam())
                .build();
    }

    private DepartmentEntity getDepartment() {
        return DepartmentEntity.builder()
                .id(1L)
                .name("Sample department")
                .status(ACTIVE)
                .build();
    }

    private TeamEntity getTeam() {
        return TeamEntity.builder()
                .id(1L)
                .name("Sample team")
                .status(ACTIVE)
                .department(getDepartment())
                .build();
    }

    private RequiredSkillEntity getRequiredSkillEntity() {
        return RequiredSkillEntity.builder()
                .id(1L)
                .skill(getSkillEntity())
                .position(getPositionEntity())
                .requiredTopicList(getRequiredTopicEntityList())
                .build();
    }

    private RequiredSkillEntity getRequiredSkillEntity2() {
        return RequiredSkillEntity.builder()
                .id(1L)
                .skill(getSkillEntity2())
                .position(getPositionEntity())
                .requiredTopicList(getRequiredTopicEntityList())
                .build();
    }

    private RequiredSkill getRequiredSkill() {
        return RequiredSkill.builder()
                .id(1L)
                .skillId(1L)
                .positionId(1L)
                .requiredTopicList(getRequiredTopicList())
                .build();
    }

    private List<RequiredTopic> getRequiredTopicList() {
        return Arrays.asList(getRequiredTopic1(), getRequiredTopic2());
    }

    private List<RequiredTopic> getDuplicatedRequiredTopicList() {
        return Arrays.asList(getRequiredTopic1(), getRequiredTopic1());
    }

    private List<RequiredTopicEntity> getRequiredTopicEntityList() {
        return Arrays.asList(getRequiredTopicEntity1(), getRequiredTopicEntity2());
    }
}