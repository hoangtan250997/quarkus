package com.axonactive.agileskills.position.requiredskill.service;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.dao.RequiredSkillDAO;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredTopic;
import com.axonactive.agileskills.position.service.model.Position;
import com.axonactive.agileskills.skill.dao.SkillDAO;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.axonactive.agileskills.base.entity.StatusEnum.ACTIVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.REQUIRED_SKILL_LEVEL_MUST_BE_MASTER;
import static com.axonactive.agileskills.base.exception.ErrorMessage.REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_ID_DUPLICATED;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_LEVEL_MUST_BE_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_NOT_FOUND;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE;
import static com.axonactive.agileskills.position.entity.PositionStatusEnum.OPEN;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.MASTER;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.USED;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.MUST_HAVE;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.NICE_TO_HAVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class RequiredSkillServiceTest {

    @InjectMocks
    private RequiredSkillService requiredSkillService;

    @Mock
    private RequiredTopicService requiredTopicService;

    @Mock
    private SkillDAO skillDAO;

    @Mock
    private RequiredSkillDAO requiredSkillDAO;

    @Test
    void deleteOldRequiredSkillList_requiredSkillListIsEmpty_keepOldRequiredSkillList() throws InputValidationException, ResourceNotFoundException {
        PositionEntity updatedPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        requiredSkillService.deleteRequiredSkillAndTopicList(updatedPositionEntity);

        verifyNoInteractions(requiredSkillDAO);
    }

    @Test
    void deleteOldRequiredSkillList_requiredSkillListIsNotEmpty_deleteOldRequiredSkillList() throws InputValidationException, ResourceNotFoundException {
        PositionEntity updatedPositionEntity = getPositionEntity();
        RequiredSkillEntity requiredSkillEntity = getRequiredSkillEntity();
        updatedPositionEntity.setRequiredSkillList(Collections.singletonList(requiredSkillEntity));

        requiredSkillService.deleteRequiredSkillAndTopicList(updatedPositionEntity);

        verify(requiredSkillDAO).deleteInList(anyList());
    }

    @Test
    void createEntityList_RequireMustHaveTopicWithoutRequireMustHaveSkill_ThrowException() {
        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(MUST_HAVE);
        inputPosition.getRequiredSkillList().get(0).setRequire(NICE_TO_HAVE);
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(InputValidationException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition), REQUIRED_SKILL_REQUIRE_MUST_BE_MUST_HAVE);
    }

    @Test
    void createEntityList_LevelMasterTopicWithoutLevelMasterSkill_ThrowException() {
        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(MASTER);
        inputPosition.getRequiredSkillList().get(0).setLevel(USED);
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(InputValidationException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition), REQUIRED_SKILL_LEVEL_MUST_BE_MASTER);
    }

    @Test
    void createEntityList_validPosition_returnList() throws InputValidationException, ResourceNotFoundException {
        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.ofNullable(getSkillEntity()));
        when(requiredTopicService.createEntityList(any(RequiredSkillEntity.class), any(RequiredSkill.class))).thenReturn(getRequiredTopicEntityList());

        Position inputPosition = getPositionWithOneRequiredSkill();
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        List<RequiredSkillEntity> resultRequiredSkillList = requiredSkillService.createEntityList(createdPositionEntity, inputPosition);

        verify(requiredTopicService).createEntityList(any(RequiredSkillEntity.class), any(RequiredSkill.class));

        assertEquals(inputPosition.getRequiredSkillList().size(), resultRequiredSkillList.size());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().size(), resultRequiredSkillList.get(0).getRequiredTopicList().size());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getSkillId(), resultRequiredSkillList.get(0).getSkill().getId());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(0).getTopicId(), resultRequiredSkillList.get(0).getRequiredTopicList().get(0).getId());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(1).getTopicId(), resultRequiredSkillList.get(0).getRequiredTopicList().get(1).getId());
    }

    @Test
    void createEntityList_noteIsNull_returnList() throws InputValidationException, ResourceNotFoundException {
        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.ofNullable(getSkillEntity()));
        when(requiredTopicService.createEntityList(any(RequiredSkillEntity.class), any(RequiredSkill.class))).thenReturn(getRequiredTopicEntityList());

        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).setNote(null);

        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();
        List<RequiredSkillEntity> resultRequiredSkillList = requiredSkillService.createEntityList(createdPositionEntity, inputPosition);

        verify(requiredTopicService).createEntityList(any(RequiredSkillEntity.class), any(RequiredSkill.class));

        assertEquals(inputPosition.getRequiredSkillList().size(), resultRequiredSkillList.size());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().size(), resultRequiredSkillList.get(0).getRequiredTopicList().size());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getSkillId(), resultRequiredSkillList.get(0).getSkill().getId());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(0).getTopicId(), resultRequiredSkillList.get(0).getRequiredTopicList().get(0).getId());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(1).getTopicId(), resultRequiredSkillList.get(0).getRequiredTopicList().get(1).getId());
    }

    @Test
    void createEntityList_noteLengthIs2000Chars_returnList() throws InputValidationException, ResourceNotFoundException {
        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.ofNullable(getSkillEntity()));
        when(requiredTopicService.createEntityList(any(RequiredSkillEntity.class), any(RequiredSkill.class))).thenReturn(getRequiredTopicEntityList());

        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).setNote("a".repeat(2000));

        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();
        List<RequiredSkillEntity> resultRequiredSkillList = requiredSkillService.createEntityList(createdPositionEntity, inputPosition);

        verify(requiredTopicService).createEntityList(any(RequiredSkillEntity.class), any(RequiredSkill.class));

        assertEquals(inputPosition.getRequiredSkillList().size(), resultRequiredSkillList.size());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().size(), resultRequiredSkillList.get(0).getRequiredTopicList().size());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getSkillId(), resultRequiredSkillList.get(0).getSkill().getId());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(0).getTopicId(), resultRequiredSkillList.get(0).getRequiredTopicList().get(0).getId());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getRequiredTopicList().get(1).getTopicId(), resultRequiredSkillList.get(0).getRequiredTopicList().get(1).getId());
    }

    @Test
    void createEntityList_skillWithoutTopic_returnList() throws InputValidationException, ResourceNotFoundException {
        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.ofNullable(getSkillEntity()));

        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).setRequiredTopicList(null);
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        List<RequiredSkillEntity> resultRequiredSkillList = requiredSkillService.createEntityList(createdPositionEntity, inputPosition);
        assertEquals(inputPosition.getRequiredSkillList().size(), resultRequiredSkillList.size());
        assertNull(resultRequiredSkillList.get(0).getRequiredTopicList());
        assertEquals(inputPosition.getRequiredSkillList().get(0).getSkillId(), resultRequiredSkillList.get(0).getSkill().getId());
    }

    @Test
    void createEntityList_duplicateSkill_throwException() {
        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.setRequiredSkillList(getDuplicateRequiredSkillList());
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(InputValidationException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition), SKILL_ID_DUPLICATED);
    }

    @Test
    void createEntityList_skillNotFound_throwException() {
        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.empty());

        Position inputPosition = getPositionWithOneRequiredSkill();
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(ResourceNotFoundException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition), SKILL_NOT_FOUND);
    }

    @Test
    void createEntityList_requiredWithoutLevel_throwException() {
        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).setLevel(null);
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(InputValidationException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition), SKILL_LEVEL_MUST_BE_GREATER_THAN_0);
    }

    @Test
    void createEntityList_levelWithoutRequire_throwException() {
        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).setRequire(null);
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(InputValidationException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition), SKILL_REQUIRE_MUST_BE_MUST_HAVE_OR_NICE_TO_HAVE);
    }

    @Test
    void createEntityList_topicRequiredWithoutSkillRequired_throwException() {
        Position inputPosition = getPositionWithOneRequiredSkill();
        inputPosition.getRequiredSkillList().get(0).setRequire(null);
        inputPosition.getRequiredSkillList().get(0).setLevel(null);
        PositionEntity createdPositionEntity = getPositionEntityWithEmptyRequiredSkill();

        assertThrows(InputValidationException.class, () -> requiredSkillService.createEntityList(createdPositionEntity, inputPosition));
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

    private PositionEntity getPositionEntity() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample Position")
                .quantity(10)
                .status(OPEN)
                .team(getTeam())
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

    private Position getPositionWithOneRequiredSkill() {
        return Position.builder()
                .name("Input Position DTO")
                .note("One Required Skill")
                .quantity(2)
                .teamId(1L)
                .requiredSkillList(getRequiredSkillList())
                .build();
    }

    private List<RequiredSkill> getDuplicateRequiredSkillList() {
        return Arrays.asList(getInputRequiredSkill1(), getInputRequiredSkill1());
    }

    private List<RequiredSkill> getRequiredSkillList() {
        return Collections.singletonList(getInputRequiredSkill1());
    }

    private RequiredSkill getInputRequiredSkill1() {
        return RequiredSkill.builder()
                .skillId(1L)
                .require(MUST_HAVE)
                .level(MASTER)
                .note("Input DTO Required Skill 1")
                .requiredTopicList(getRequiredTopicList())
                .build();
    }

    private List<RequiredTopic> getRequiredTopicList() {
        return Arrays.asList(getInputRequiredTopic1(), getInputRequiredTopic2());
    }

    private RequiredTopic getInputRequiredTopic1() {
        return RequiredTopic.builder()
                .topicId(1L)
                .requiredSkillId(1L)
                .level(MASTER)
                .require(MUST_HAVE)
                .note("Input DTO Required Topic 1")
                .build();
    }

    private RequiredTopic getInputRequiredTopic2() {
        return RequiredTopic.builder()
                .topicId(2L)
                .requiredSkillId(1L)
                .level(null)
                .require(null)
                .note("Input DTO Required Topic 2")
                .build();
    }

    private PositionEntity getPositionEntityWithEmptyRequiredSkill() {
        return PositionEntity.builder()
                .name("Just created Position Entity")
                .note("Empty Required Skill List")
                .id(1L)
                .quantity(2)
                .status(OPEN)
                .createdDate(LocalDateTime.parse("2023-01-01T09:00:00"))
                .build();
    }

    private SkillEntity getSkillEntity() {
        return SkillEntity.builder()
                .id(1L)
                .name("Sample Skill returned")
                .description("Id 1")
                .status(ACTIVE)
                .build();
    }

    private List<RequiredTopicEntity> getRequiredTopicEntityList() {
        return Arrays.asList(getRequiredTopicEntity1(), getRequiredTopicEntity2());
    }

    private RequiredTopicEntity getRequiredTopicEntity1() {
        return RequiredTopicEntity.builder()
                .id(1L)
                .topic(TopicEntity.builder().id(1L).build())
                .build();
    }

    private RequiredTopicEntity getRequiredTopicEntity2() {
        return RequiredTopicEntity.builder()
                .id(2L)
                .topic(TopicEntity.builder().id(2L).build())
                .build();
    }
}