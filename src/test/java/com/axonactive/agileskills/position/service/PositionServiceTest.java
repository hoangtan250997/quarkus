package com.axonactive.agileskills.position.service;

import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.team.dao.TeamDAO;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.position.dao.PositionDAO;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.entity.LevelEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequireEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredTopicEntity;
import com.axonactive.agileskills.position.requiredskill.service.RequiredSkillService;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredTopic;
import com.axonactive.agileskills.position.service.mapper.PositionMapper;
import com.axonactive.agileskills.position.service.model.Position;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.axonactive.agileskills.base.entity.StatusEnum.ACTIVE;
import static com.axonactive.agileskills.position.entity.PositionStatusEnum.CLOSE;
import static com.axonactive.agileskills.position.entity.PositionStatusEnum.OPEN;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.LEARNED;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.MASTER;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.MUST_HAVE;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.NICE_TO_HAVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class PositionServiceTest {

    @InjectMocks
    private PositionService positionService;

    @Mock
    private PositionDAO positionDAO;

    @Mock
    private TeamDAO teamDAO;

    @Mock
    private PositionMapper positionMapper;

    @Mock
    private RequiredSkillService requiredSkillService;

    @Mock
    private PositionListCache positionListCache;

    @Mock
    private SearchPositionCache searchPositionCache;

    @Test
    void updatePosition_PositionNotFound_ThrowException() {
        Position updatedPosition = getPosition();
        when(positionDAO.findByIdAndStatus(999L, OPEN)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> positionService.update(999L, updatedPosition));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionNameInvalid_ThrowConstraintViolationException() {
        Position nullNamePosition = getPositionWithName(null);
        Position blankNamePosition = getPositionWithName(" ");
        Position emptyNamePosition = getPositionWithName("");
        Position lessThan3CharsPosition = getPositionWithName("A");
        Position moreThan255CharsPosition = getPositionWithName("A".repeat(256));

        Arrays.asList(nullNamePosition, blankNamePosition, emptyNamePosition, lessThan3CharsPosition, moreThan255CharsPosition)
                .forEach(position -> assertThrows(ConstraintViolationException.class, () -> positionService.update(1L, position)));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionNoteIsMoreThan2000Chars_ThrowConstraintViolationException() {
        Position moreThan2000CharsNotePosition = getPositionWithNote("A".repeat(2001));
        moreThan2000CharsNotePosition.setTeamId(1L);

        assertThrows(ConstraintViolationException.class, () -> positionService.update(1L, moreThan2000CharsNotePosition));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionQuantityIsInvalid_ThrowConstraintViolationException() {
        Position nullQuantityPosition = getPositionWithQuantity(null);
        nullQuantityPosition.setTeamId(1L);
        Position lowerThan1QuantityPosition = getPositionWithQuantity(0);
        lowerThan1QuantityPosition.setTeamId(1L);

        Arrays.asList(nullQuantityPosition, lowerThan1QuantityPosition)
                .forEach(position -> assertThrows(ConstraintViolationException.class, () -> positionService.update(1L, position)));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionTeamNotFoundOrInactive_ThrowResourceNotFoundException() {
        PositionEntity positionEntity = getOpenPositionEntity();

        Position position = getOpenPositionDTO();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));
        when(teamDAO.findByIdAndStatus(position.getTeamId(), ACTIVE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> positionService.update(1L, position));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionNameDuplicated_ThrowException() {
        PositionEntity positionEntity = getOpenPositionEntity2();
        Position position = getDuplicatedNamePosition();
        List<PositionEntity> positionList = getPositionList();

        when(positionDAO.findByTeamIdAndStatus(1L, OPEN)).thenReturn(positionList);
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        assertThrows(InputValidationException.class, () -> positionService.update(1L, position));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionValidField_UpdateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity teamEntity = getActiveTeam();
        PositionEntity foundPositionEntity = getOpenPositionEntity2();
        PositionEntity returnedPositionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(teamEntity));
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(foundPositionEntity));
        when(positionDAO.update(any())).thenReturn(returnedPositionEntity);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        PositionEntity updatedPosition = positionService.update(1L, position);

        assertEquals(returnedPositionEntity.getId(), updatedPosition.getId());
        assertEquals(returnedPositionEntity.getName(), updatedPosition.getName());
        assertEquals(returnedPositionEntity.getNote(), updatedPosition.getNote());
        assertEquals(returnedPositionEntity.getQuantity(), updatedPosition.getQuantity());
        assertEquals(returnedPositionEntity.getStatus(), updatedPosition.getStatus());
        assertEquals(returnedPositionEntity.getTeam().getId(), updatedPosition.getTeam().getId());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionValidFieldWithNullRequiredSkillList_UpdateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity teamEntity = getActiveTeam();
        PositionEntity foundPositionEntity = getPositionEntityWithRequiredSkillEntityList();
        PositionEntity returnedPositionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(null);


        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(teamEntity));
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(foundPositionEntity));
        when(positionDAO.update(any())).thenReturn(returnedPositionEntity);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        PositionEntity updatedPosition = positionService.update(1L, position);

        assertEquals(returnedPositionEntity.getId(), updatedPosition.getId());
        assertEquals(returnedPositionEntity.getName(), updatedPosition.getName());
        assertEquals(returnedPositionEntity.getNote(), updatedPosition.getNote());
        assertEquals(returnedPositionEntity.getQuantity(), updatedPosition.getQuantity());
        assertEquals(returnedPositionEntity.getStatus(), updatedPosition.getStatus());
        assertEquals(returnedPositionEntity.getTeam().getId(), updatedPosition.getTeam().getId());
        assertNull(updatedPosition.getRequiredSkillList());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionValidFieldWithRequiredSkillList_UpdateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity teamEntity = getActiveTeam();
        PositionEntity foundPositionEntity = getOpenPositionEntity2();
        PositionEntity returnedPositionEntity = getOpenPositionEntity();
        returnedPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());
        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(getRequiredSkillList());


        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(teamEntity));
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(foundPositionEntity));
        when(positionDAO.update(any())).thenReturn(returnedPositionEntity);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        PositionEntity updatedPosition = positionService.update(1L, position);

        assertEquals(returnedPositionEntity.getId(), updatedPosition.getId());
        assertEquals(returnedPositionEntity.getName(), updatedPosition.getName());
        assertEquals(returnedPositionEntity.getNote(), updatedPosition.getNote());
        assertEquals(returnedPositionEntity.getQuantity(), updatedPosition.getQuantity());
        assertEquals(returnedPositionEntity.getStatus(), updatedPosition.getStatus());
        assertEquals(returnedPositionEntity.getTeam().getId(), updatedPosition.getTeam().getId());
        assertEquals(returnedPositionEntity.getRequiredSkillList().size(), updatedPosition.getRequiredSkillList().size());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getSkill().getName(), updatedPosition.getRequiredSkillList().get(0).getSkill().getName());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getSkill().getDescription(), updatedPosition.getRequiredSkillList().get(0).getSkill().getDescription());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getRequire(), updatedPosition.getRequiredSkillList().get(0).getRequire());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getNote(), updatedPosition.getRequiredSkillList().get(0).getNote());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void updatePosition_PositionEntityFoundWithRequiredSkillAndTopicList_deleteOldRequiredSkillAndTopicListAndUpdateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity teamEntity = getActiveTeam();
        PositionEntity foundPositionEntity = getOpenPositionEntity2();
        foundPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());

        PositionEntity returnedPositionEntity = getOpenPositionEntity();
        returnedPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());

        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(getRequiredSkillList());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(teamEntity));
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(foundPositionEntity));
        when(positionDAO.update(any())).thenReturn(returnedPositionEntity);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());
        PositionEntity updatedPosition = positionService.update(1L, position);

        assertEquals(returnedPositionEntity.getId(), updatedPosition.getId());
        assertEquals(returnedPositionEntity.getName(), updatedPosition.getName());
        assertEquals(returnedPositionEntity.getNote(), updatedPosition.getNote());
        assertEquals(returnedPositionEntity.getQuantity(), updatedPosition.getQuantity());
        assertEquals(returnedPositionEntity.getStatus(), updatedPosition.getStatus());
        assertEquals(returnedPositionEntity.getTeam().getId(), updatedPosition.getTeam().getId());
        verify(requiredSkillService).deleteRequiredSkillAndTopicList(foundPositionEntity);
        assertEquals(returnedPositionEntity.getRequiredSkillList().size(), updatedPosition.getRequiredSkillList().size());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getSkill().getName(), updatedPosition.getRequiredSkillList().get(0).getSkill().getName());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getSkill().getDescription(), updatedPosition.getRequiredSkillList().get(0).getSkill().getDescription());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getRequire(), updatedPosition.getRequiredSkillList().get(0).getRequire());
        assertEquals(returnedPositionEntity.getRequiredSkillList().get(0).getNote(), updatedPosition.getRequiredSkillList().get(0).getNote());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void updatePosition_PositionEntityFound_InputRequiredSkillNoteMoreThan2000Chars_ThrowConstraintValidationException() {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        PositionEntity returnedPositionEntity = getOpenPositionEntity();
        returnedPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());
        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(getRequiredSkillList2());

        assertThrows(ConstraintViolationException.class, () -> positionService.update(1L, position));
    }

    @Test
    void updatePosition_PositionEntityFound_InputRequiredTopicNoteMoreThan2000Chars_ThrowConstraintValidationException() {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        PositionEntity returnedPositionEntity = getOpenPositionEntity();
        returnedPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());
        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(getRequiredSkillList3());

        assertThrows(ConstraintViolationException.class, () -> positionService.update(1L, position));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionNameNotNullAndNotEqualToPositionEntityName_CheckDuplicateName_InputPositionNameDuplicated_ThrowInputValidationException() {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity2();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        PositionEntity foundPositionEntity = getOpenPositionEntity2();
        PositionEntity returnedPositionEntity = getOpenPositionEntity();

        returnedPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());
        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(getRequiredSkillList());

        when(positionDAO.findByTeamIdAndStatus(1L, OPEN)).thenReturn(positionEntityList);
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(foundPositionEntity));

        assertThrows(InputValidationException.class, () -> positionService.update(1L, position));
    }

    @Test
    void updatePosition_PositionEntityFound_InputPositionNameNotNullAndNotEqualToPositionEntityName_CheckDuplicateName_InputPositionNameNotDuplicated_UpdateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity2();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity teamEntity = getActiveTeam();
        PositionEntity foundPositionEntity = getOpenPositionEntity2();
        PositionEntity returnedPositionEntity = getOpenPositionEntity1();

        returnedPositionEntity.setRequiredSkillList(getRequiredSkillEntityList());
        Position position = getOpenPositionDTO1();
        position.setRequiredSkillList(getRequiredSkillList());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(teamEntity));
        when(positionDAO.findByTeamIdAndStatus(1L, OPEN)).thenReturn(positionEntityList);
        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(foundPositionEntity));
        when(positionDAO.update(any())).thenReturn(returnedPositionEntity);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        PositionEntity updatedPosition = positionService.update(1L, position);

        assertEquals(returnedPositionEntity.getId(), updatedPosition.getId());
        assertEquals(returnedPositionEntity.getName(), updatedPosition.getName());
        assertEquals(returnedPositionEntity.getNote(), updatedPosition.getNote());
        assertEquals(returnedPositionEntity.getQuantity(), updatedPosition.getQuantity());
        assertEquals(returnedPositionEntity.getStatus(), updatedPosition.getStatus());
        assertEquals(returnedPositionEntity.getTeam().getId(), updatedPosition.getTeam().getId());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createPosition_openedDateHasOffsetDifferentWithServer_ReturnPositionWithOpenedDateHasSameOffsetWithServer() {
        TeamEntity team = getActiveTeam();

        Position position = getOpenPositionDTO();
        position.setOpenedDate(OffsetDateTime.of(2023, 05, 05, 10, 10, 10, 000, ZoneOffset.of("+02:00")));

        PositionEntity createdPosition = positionService.createNewPosition(position, team);

        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getOpenedDate().plusHours(5).toLocalDateTime(), createdPosition.getOpenedDate());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_PositionNotFound_ThrowException() {
        when(positionDAO.findByIdAndStatus(999L, OPEN)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> positionService.displayPositionWithRequiredSkillAndRequiredTopic(999L));
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredTopicHasRequireAndLevelAndNoteNull_ReturnPositionHas1RqSkillAnd1RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredTopicHasRequireAndLevelNullAndNoteEmpty_ReturnPositionHas1RqSkillAnd1RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote("");

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredTopicHasOnlyNoteNull_ReturnPositionHas1RqSkillAnd2RqTopics()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(2, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredTopicHasOnlyNoteEmpty_ReturnPositionHas1RqSkillAnd2RqTopics()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote("");

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(2, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillAndAllTopicsHasRequireAndLevelAndNoteNull_ReturnPositionWithNoRqSkillAndTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setNote(null);
        positionEntity.getRequiredSkillList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).setNote(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(0, displayedPosition.getRequiredSkillList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillAndTopicHasRequireAndLevelAndNoteNull_ReturnPositionHas1RqSkillAnd1RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);
        positionEntity.getRequiredSkillList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).setNote(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().size());
        assertEquals(1, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillHasRequireAndLevelAndNoteNullButHave2DisplayedTopics_ReturnPositionHas1RqSkillAnd2RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).setNote(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().size());
        assertEquals(2, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillHasRequireAndLevelNullButHave2DisplayedTopics_ReturnPositionHas1RqSkillAnd2RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).setLevel(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().size());
        assertEquals(2, displayedPosition.getRequiredSkillList().get(0).getRequiredTopicList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillHasOnlyNoteNull_ReturnPositionHas1RqSkillAnd2RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setNote(null);
        positionEntity.getRequiredSkillList().get(0).setNote(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillHasOnlyNoteEmpty_ReturnPositionHas1RqSkillAnd2RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setNote(null);
        positionEntity.getRequiredSkillList().get(0).setNote("");

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().size());
    }

    @Test
    void displayPositionWithRequiredSkillAndRequiredTopic_RequiredSkillHasRequireAndLevelNull_ReturnPositionHas1RqSkillAnd2RqTopic()
            throws ResourceNotFoundException {
        PositionEntity positionEntity = getPositionEntity();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));

        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(0).setNote(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setLevel(null);
        positionEntity.getRequiredSkillList().get(0).getRequiredTopicList().get(1).setNote(null);
        positionEntity.getRequiredSkillList().get(0).setRequire(null);
        positionEntity.getRequiredSkillList().get(0).setLevel(null);

        PositionEntity displayedPosition = positionService.displayPositionWithRequiredSkillAndRequiredTopic(positionEntity.getId());

        assertEquals(1, displayedPosition.getRequiredSkillList().size());
    }

    @Test
    void getPositionList_ByStatusOpenWhenCacheAvailable_ReturnDTOList() {
        List<PositionEntity> positionEntityList = getListOfPositionEntity();
        List<Position> positionsDtoList = getListOfPositionDTO();

        PositionListCache exampleCache = new PositionListCache();
        exampleCache.getCache().put("OPEN", positionEntityList);

        when(positionListCache.getCache()).thenReturn(exampleCache.getCache());
        when(positionMapper.toDTOList(positionEntityList)).thenReturn(positionsDtoList);

        assertEquals(positionEntityList.size(), positionService.getByStatus(OPEN).size());
        verifyNoInteractions(positionDAO);
    }

    @Test
    void getPositionList_ByStatusOpenWhenCacheUnavailable_ReturnDTOList() {
        List<PositionEntity> positionEntityList = getListOfPositionEntity();
        List<Position> positionsDtoList = getListOfPositionDTO();

        PositionListCache exampleCache = new PositionListCache();

        when(positionListCache.getCache()).thenReturn(exampleCache.getCache());
        when(positionDAO.findByStatus(OPEN)).thenReturn(positionEntityList);
        when(positionMapper.toDTOList(positionEntityList)).thenReturn(positionsDtoList);

        assertEquals(positionEntityList.size(), positionService.getByStatus(OPEN).size());
        verify(positionDAO).findByStatus(OPEN);
    }

    @Test
    void getPositionList_ByStatusCloseWhenCacheAvailable_ReturnDTOList() {
        List<PositionEntity> positionEntityList = getListOfPositionEntity();
        List<Position> positionsDtoList = getListOfPositionDTO();

        PositionListCache exampleCache = new PositionListCache();
        exampleCache.getCache().put("CLOSE", positionEntityList);

        when(positionListCache.getCache()).thenReturn(exampleCache.getCache());
        when(positionMapper.toDTOList(positionEntityList)).thenReturn(positionsDtoList);

        assertEquals(positionEntityList.size(), positionService.getByStatus(CLOSE).size());
        verifyNoInteractions(positionDAO);
    }

    @Test
    void getPositionList_ByStatusCloseWhenCacheUnavailable_ReturnDTOList() {
        List<PositionEntity> positionEntityList = getListOfPositionEntity();
        List<Position> positionsDtoList = getListOfPositionDTO();

        PositionListCache exampleCache = new PositionListCache();

        when(positionListCache.getCache()).thenReturn(exampleCache.getCache());
        when(positionDAO.findByStatus(CLOSE)).thenReturn(positionEntityList);
        when(positionMapper.toDTOList(positionEntityList)).thenReturn(positionsDtoList);

        assertEquals(positionEntityList.size(), positionService.getByStatus(CLOSE).size());
        verify(positionDAO).findByStatus(CLOSE);
    }

    @Test
    void closePosition_NonExistedPosition_ThrowResourceNotFoundException() {
        when(positionDAO.close(1L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> positionService.close(1L));
    }

    @Test
    void closePosition_ExistingPosition_CloseSuccessFully() throws ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        when(positionDAO.close(any())).thenReturn(getClosePositionEntity());
        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        positionService.close(1L);

        verify(positionDAO, times(1)).close(any());
        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createPosition_NameIsInvalid_ThrowConstraintViolationException() {
        Position nullNamePosition = getPositionWithName(null);
        Position blankNamePosition = getPositionWithName(" ");
        Position emptyNamePosition = getPositionWithName("");
        Position lessThan3CharsPosition = getPositionWithName("A");
        Position moreThan255CharsPosition = getPositionWithName("A".repeat(256));

        Arrays.asList(nullNamePosition, blankNamePosition, emptyNamePosition, lessThan3CharsPosition, moreThan255CharsPosition)
                .forEach(position -> assertThrows(ConstraintViolationException.class, () -> positionService.createPositionWithRequiredSkill(position)));
    }

    @Test
    void createPosition_NameIsDuplicated_ThrowInputValidationException() {
        Position position = getDuplicatedNamePosition();
        List<PositionEntity> positionList = getPositionList();

        when(positionDAO.findByTeamIdAndStatus(1L, OPEN)).thenReturn(positionList);
        assertThrows(InputValidationException.class, () -> positionService.createPositionWithRequiredSkill(position));
    }

    @Test
    void createPosition_NoteIsMoreThan2000Chars_ThrowConstraintViolationException() {
        Position moreThan2000CharsNotePosition = getPositionWithNote("A".repeat(2001));
        assertThrows(ConstraintViolationException.class, () -> positionService.createPositionWithRequiredSkill(moreThan2000CharsNotePosition));
    }

    @Test
    void createPosition_QuantityIsInvalid_ThrowConstraintViolationException() {
        Position nullQuantityPosition = getPositionWithQuantity(null);
        Position lowerThan1QuantityPosition = getPositionWithQuantity(0);

        Arrays.asList(nullQuantityPosition, lowerThan1QuantityPosition)
                .forEach(position -> assertThrows(ConstraintViolationException.class, () -> positionService.createPositionWithRequiredSkill(position)));
    }

    @Test
    void createPosition_TeamNotFoundOrInactive_ThrowResourceNotFoundException() {
        Position position = getOpenPositionDTO();
        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> positionService.createPositionWithRequiredSkill(position));
    }

    @Test
    void createPosition_ValidField_CreateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();
        List<PositionEntity> filterPositionList = getListOfOpenPositionEntity();

        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", filterPositionList);

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        when(positionMapper.toDTO(positionEntity)).thenReturn(position);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        Position createdPosition = positionService.createPositionWithRequiredSkill(position);

        assertEquals(position.getId(), createdPosition.getId());
        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getTeamId(), createdPosition.getTeamId());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createPosition_NoteIsNull_CreateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();
        position.setNote(null);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        when(positionMapper.toDTO(positionEntity)).thenReturn(position);

        Position createdPosition = positionService.createPositionWithRequiredSkill(position);

        assertEquals(position.getId(), createdPosition.getId());
        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getTeamId(), createdPosition.getTeamId());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createPosition_RequiredSkillListIsNull_CreateSuccessfully() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();
        position.setRequiredSkillList(null);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        when(positionMapper.toDTO(positionEntity)).thenReturn(position);

        Position createdPosition = positionService.createPositionWithRequiredSkill(position);

        assertEquals(position.getId(), createdPosition.getId());
        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getTeamId(), createdPosition.getTeamId());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void getByIdAndStatus_ExistingIdAndStatusOpen_ReturnModel() throws ResourceNotFoundException {
        PositionEntity positionEntity = getOpenPositionEntity();
        Position positionDTO = getOpenPositionDTO();

        when(positionDAO.findByIdAndStatus(1L, OPEN)).thenReturn(Optional.of(positionEntity));
        when(positionMapper.toDTO(positionEntity)).thenReturn(positionDTO);

        Position actualPosition = positionService.getByIdAndStatus(1L, OPEN);

        assertEquals(positionDTO.getId(), actualPosition.getId());
        assertEquals(positionDTO.getName(), actualPosition.getName());
        assertEquals(positionDTO.getNote(), actualPosition.getNote());
        assertEquals(positionDTO.getQuantity(), actualPosition.getQuantity());
        assertEquals(positionDTO.getStatus(), actualPosition.getStatus());
        assertEquals(positionDTO.getTeamId(), actualPosition.getTeamId());
    }

    @Test
    void getByIdAndStatus_ExistingIdAndStatusClose_ReturnModel() throws ResourceNotFoundException {
        PositionEntity positionEntity = getClosePositionEntity();
        Position positionDTO = getClosePositionDTO();

        when(positionDAO.findByIdAndStatus(1L, CLOSE)).thenReturn(Optional.of(positionEntity));
        when(positionMapper.toDTO(positionEntity)).thenReturn(positionDTO);

        Position actualPosition = positionService.getByIdAndStatus(1L, CLOSE);

        assertEquals(positionDTO.getId(), actualPosition.getId());
        assertEquals(positionDTO.getName(), actualPosition.getName());
        assertEquals(positionDTO.getQuantity(), actualPosition.getQuantity());
        assertEquals(positionDTO.getStatus(), actualPosition.getStatus());
    }

    @Test
    void getByIdAndStatus_IdNotExisted_ThrowException() {
        when(positionDAO.findByIdAndStatus(999L, CLOSE)).thenReturn(Optional.empty());
        when(positionDAO.findByIdAndStatus(999L, OPEN)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> positionService.getByIdAndStatus(999L, CLOSE));
        assertThrows(ResourceNotFoundException.class, () -> positionService.getByIdAndStatus(999L, OPEN));
    }

    @Test
    void createPositionWithRequiredSkill_WithRequiredSkill_ReturnModel() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        when(requiredSkillService.createEntityList(any(PositionEntity.class), any(Position.class))).thenReturn(getRequiredSkillEntityList());

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();

        position.setRequiredSkillList(getRequiredSkillList());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        when(positionMapper.toDTO(any())).thenReturn(position);

        positionService.createPositionWithRequiredSkill(position);

        verify(requiredSkillService).createEntityList(any(PositionEntity.class), any(Position.class));
        verify(positionMapper).toDTO(any(PositionEntity.class));
        verify(positionDAO).create(any());
        verify(teamDAO).findByIdAndStatus(1L, ACTIVE);

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createPositionWithOpenedDate_FormatIsValid_ReturnModel() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntityWithOpenedDate();
        Position position = getOpenPositionDTO();

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        when(positionMapper.toDTO(positionEntity)).thenReturn(position);

        Position createdPosition = positionService.createPositionWithRequiredSkill(position);

        assertEquals(position.getId(), createdPosition.getId());
        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getTeamId(), createdPosition.getTeamId());
        assertEquals(position.getOpenedDate(), createdPosition.getOpenedDate());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createPosition_WithOutOpenedDate_ReturnModel() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntityWithoutInputOpenedDate();
        Position position = getOpenPositionDTO();

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        Position returnPosition = getOpenPositionDTO();
        returnPosition.setOpenedDate(OffsetDateTime.now());
        when(positionMapper.toDTO(positionEntity)).thenReturn(returnPosition);

        Position createdPosition = positionService.createPositionWithRequiredSkill(position);

        assertEquals(position.getId(), createdPosition.getId());
        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getTeamId(), createdPosition.getTeamId());
        assertEquals(returnPosition.getOpenedDate(), createdPosition.getOpenedDate());

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void createNewPosition_WithOutOpenedDate_ReturnModel() {
        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntityWithoutInputOpenedDate();

        Position position = getOpenPositionDTO();
        position.setOpenedDate(positionEntity.getOpenedDate().atOffset(ZoneOffset.of("+07:00")));

        PositionEntity createdPosition = positionService.createNewPosition(position, team);

        assertEquals(position.getName(), createdPosition.getName());
        assertEquals(position.getNote(), createdPosition.getNote());
        assertEquals(position.getQuantity(), createdPosition.getQuantity());
        assertEquals(position.getStatus(), createdPosition.getStatus());
        assertEquals(position.getOpenedDate(), createdPosition.getOpenedDate().atOffset(ZoneOffset.of("+07:00")));
    }

    @Test
    void createPositionWithRequiredSkill_WithRequiredSkillWithNullTopicList_ReturnModel() throws InputValidationException, ResourceNotFoundException {
        PositionListCache positionListCache1 = new PositionListCache();
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        positionListCache1.getCache().put("OPEN", positionEntityList);
        searchPositionCache1.getCache().put("Java", positionEntityList);

        when(requiredSkillService.createEntityList(any(PositionEntity.class), any(Position.class))).thenReturn(getRequiredSkillEntityList());

        TeamEntity team = getActiveTeam();
        PositionEntity positionEntity = getOpenPositionEntity();
        Position position = getOpenPositionDTO();

        position.setRequiredSkillList(getInputRequiredSkillListWithNullRequiredTopicList());

        when(positionListCache.getCache()).thenReturn(positionListCache1.getCache());
        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        when(teamDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(team));
        when(positionDAO.create(any())).thenReturn(positionEntity);
        when(positionMapper.toDTO(any())).thenReturn(position);

        positionService.createPositionWithRequiredSkill(position);

        verify(requiredSkillService).createEntityList(any(PositionEntity.class), any(Position.class));
        verify(positionMapper).toDTO(any(PositionEntity.class));
        verify(positionDAO).create(any());
        verify(teamDAO).findByIdAndStatus(1L, ACTIVE);

        verify(positionListCache).getCache();
        verify(searchPositionCache).getCache();

        assertNull(positionListCache.getCache().getIfPresent("OPEN"));
        assertNull(searchPositionCache.getCache().getIfPresent("Java"));

        assertEquals(0, positionListCache.getCache().estimatedSize());
        assertEquals(0, searchPositionCache.getCache().estimatedSize());
    }

    @Test
    void getByYear_PositiveCase_ReturnPositionList() {
        // Arrange
        Integer year = 2021;
        List<PositionEntity> positionEntityList = new ArrayList<>();
        positionEntityList.add(new PositionEntity());

        when(positionDAO.findByYear(year)).thenReturn(positionEntityList);

        // Act
        List<PositionEntity> result = positionService.getByYear(year);

        // Assert
        assertEquals(positionEntityList, result);
    }

    @Test
    void getYears_PositiveCase_ReturnYearList() {
        List<Integer> mockYears = new ArrayList<>();
        mockYears.add(2019);
        mockYears.add(2020);
        mockYears.add(2021);

        when(positionDAO.findYears()).thenReturn(mockYears);

        List<Integer> result = positionService.getYears();

        assertEquals(mockYears, result);
    }

    private PositionEntity getPositionEntity() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample Position")
                .quantity(10)
                .status(OPEN)
                .team(getActiveTeam())
                .note("a sample position.")
                .requiredSkillList(getRequiredSkillEntityList())
                .build();
    }

    @Test
    void searchOpenPositionsByWord_IncorrectWord_ReturnEmptyList() {
        SearchPositionCache searchPositionCache1 = new SearchPositionCache();
        List<PositionEntity> positionEntityList = new ArrayList<>();

        String word = "Not Exist word";

        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());
        when(positionDAO.searchStatus(word, OPEN)).thenReturn(positionEntityList);

        List<Position> actualPositions = positionService.searchOpenPositionsByWord(word);
        assertEquals(0, actualPositions.size());
    }

    @Test
    void searchOpenPositionsByWord_WordIsBlankAndCacheAvailable_ReturnPositionList() {
        String word = "     ";

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        SearchPositionCache searchPositionCache1 = new SearchPositionCache();
        searchPositionCache1.getCache().put(word.trim(), positionEntityList);

        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        List<Position> positionList = getListOfOpenPositionDTO();

        when(positionMapper.toDTOList(positionEntityList)).thenReturn(positionList);

        List<Position> actualPositions = positionService.searchOpenPositionsByWord(word);
        assertEquals(positionList.size(), actualPositions.size());
    }

    @Test
    void searchOpenPositionsByWord_WordIsBlankAndCacheUnavailable_ReturnPositionList() {
        String word = "     ";

        List<PositionEntity> positionEntityList = getListOfOpenPositionEntity();

        SearchPositionCache searchPositionCache1 = new SearchPositionCache();

        when(searchPositionCache.getCache()).thenReturn(searchPositionCache1.getCache());

        List<Position> positionList = getListOfOpenPositionDTO();
        when(positionDAO.searchStatus(word, OPEN)).thenReturn(positionEntityList);
        when(positionMapper.toDTOList(positionEntityList)).thenReturn(positionList);

        List<Position> actualPositions = positionService.searchOpenPositionsByWord(word);
        assertEquals(positionList.size(), actualPositions.size());
    }

    private Position getPosition() {
        return Position.builder()
                .id(1L)
                .name("Sample Position")
                .quantity(10)
                .status(OPEN)
                .note("a sample position.")
                .requiredSkillList(getRequiredSkillList())
                .build();
    }

    private List<RequiredSkill> getRequiredSkillList() {
        return Collections.singletonList(getInputRequiredSkill1());
    }

    private RequiredSkill getInputRequiredSkill1() {
        return RequiredSkill.builder()
                .id(1L)
                .skillId(1L)
                .require(MUST_HAVE)
                .level(MASTER)
                .note("Input DTO Required Skill 1")
                .requiredTopicList(getRequiredTopicList1())
                .build();
    }

    private RequiredSkill getInputRequiredSkill2() {
        return RequiredSkill.builder()
                .id(2L)
                .skillId(2L)
                .require(MUST_HAVE)
                .level(MASTER)
                .note("a".repeat(2001))
                .requiredTopicList(getRequiredTopicList1())
                .build();
    }

    private List<RequiredSkill> getRequiredSkillList2() {
        return Collections.singletonList(getInputRequiredSkill2());
    }


    private RequiredSkill getInputRequiredSkillWithNullRequiredTopicList() {
        return RequiredSkill.builder()
                .skillId(2L)
                .require(RequireEnum.MUST_HAVE)
                .level(LevelEnum.MASTER)
                .note("Input DTO Required Skill 1")
                .requiredTopicList(null)
                .build();
    }

    private RequiredSkill getInputRequiredSkillWithInvalidTopicList() {
        return RequiredSkill.builder()
                .skillId(2L)
                .require(RequireEnum.MUST_HAVE)
                .level(LevelEnum.MASTER)
                .note("Input DTO Required Skill 1")
                .requiredTopicList(getRequiredTopicList2())
                .build();
    }

    private List<RequiredSkill> getRequiredSkillList3() {
        return Collections.singletonList(getInputRequiredSkillWithInvalidTopicList());
    }

    private List<RequiredSkill> getInputRequiredSkillListWithNullRequiredTopicList() {
        return Collections.singletonList(getInputRequiredSkillWithNullRequiredTopicList());
    }

    private List<RequiredTopic> getRequiredTopicList1() {
        return Arrays.asList(getInputRequiredTopic1(), getInputRequiredTopic2());
    }

    private RequiredTopic getInputRequiredTopic1() {
        return RequiredTopic.builder()
                .id(1L)
                .topicId(1L)
                .requiredSkillId(1L)
                .level(MASTER)
                .require(MUST_HAVE)
                .note("Input DTO Required Topic 1")
                .build();
    }

    private RequiredTopic getInputRequiredTopic2() {
        return RequiredTopic.builder()
                .id(1L)
                .topicId(2L)
                .requiredSkillId(1L)
                .level(MASTER)
                .require(MUST_HAVE)
                .note("Input DTO Required Topic 2")
                .build();
    }

    private RequiredTopic getInputRequiredTopic3() {
        return RequiredTopic.builder()
                .id(2L)
                .topicId(3L)
                .requiredSkillId(1L)
                .level(MASTER)
                .require(MUST_HAVE)
                .note("a".repeat(2001))
                .build();
    }

    private List<RequiredTopic> getRequiredTopicList2() {
        return Arrays.asList(getInputRequiredTopic1(), getInputRequiredTopic2(), getInputRequiredTopic3());
    }

    private RequiredSkillEntity getRequiredSkillEntity() {
        return RequiredSkillEntity.builder()
                .id(1L)
                .skill(getSkillEntity())
                .level(MASTER)
                .require(MUST_HAVE)
                .note("A sample required skill.")
                .requiredTopicList(getRequiredTopicList())
                .build();
    }

    private RequiredSkillEntity getRequiredSkillEntity2() {
        return RequiredSkillEntity.builder()
                .id(2L)
                .skill(getSkillEntity())
                .level(LEARNED)
                .require(NICE_TO_HAVE)
                .note("A sample required skill 2.")
                .requiredTopicList(getRequiredTopicList())
                .build();
    }

    private List<RequiredSkillEntity> getRequiredSkillEntityList() {
        return Arrays.asList(getRequiredSkillEntity());
    }

    private PositionEntity getPositionEntityWithRequiredSkillEntityList() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position")
                .note("A sample position")
                .quantity(8)
                .status(OPEN)
                .requiredSkillList(Arrays.asList(getRequiredSkillEntity(), getRequiredSkillEntity2()))
                .openedDate(LocalDateTime.now())
                .team(getActiveTeam())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private RequiredTopicEntity getRequiredTopicEntity1() {
        return RequiredTopicEntity.builder()
                .id(1L)
                .topic(getTopicEntity1())
                .level(MASTER)
                .require(MUST_HAVE)
                .note("A sample Rq Topic")
                .build();
    }

    private RequiredTopicEntity getRequiredTopicEntity2() {
        return RequiredTopicEntity.builder()
                .id(2L)
                .topic(getTopicEntity2())
                .level(MASTER)
                .require(MUST_HAVE)
                .note("A sample Rq Topic")
                .build();
    }

    private List<RequiredTopicEntity> getRequiredTopicList() {
        return Arrays.asList(getRequiredTopicEntity1(), getRequiredTopicEntity2());
    }

    private TopicEntity getTopicEntity1() {
        return TopicEntity.builder()
                .id(1L)
                .name("OOP")
                .description("OOP is fun")
                .skill(getSkillEntity())
                .build();
    }

    private TopicEntity getTopicEntity2() {
        return TopicEntity.builder()
                .id(2L)
                .name("Java 8")
                .description("Java 8 is fun")
                .skill(getSkillEntity())
                .build();
    }

    private SkillEntity getSkillEntity() {
        return SkillEntity.builder()
                .id(1L)
                .name("Java")
                .description("Java is fun")
                .status(ACTIVE)
                .build();
    }

    private DepartmentEntity getDepartment() {
        return DepartmentEntity.builder()
                .id(1L)
                .name("Sample department")
                .status(ACTIVE)
                .build();
    }

    private TeamEntity getActiveTeam() {
        return TeamEntity.builder()
                .id(1L)
                .name("Sample team")
                .status(ACTIVE)
                .department(getDepartment())
                .build();
    }

    private PositionEntity getOpenPositionEntity() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position")
                .note("A sample position")
                .quantity(8)
                .status(OPEN)
                .openedDate(LocalDateTime.now())
                .team(getActiveTeam())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private PositionEntity getOpenPositionEntity1() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position 1")
                .note("A sample position 1")
                .quantity(8)
                .status(OPEN)
                .openedDate(LocalDateTime.now())
                .team(getActiveTeam())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private PositionEntity getOpenPositionEntity2() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position 2")
                .note("A sample position 2")
                .quantity(8)
                .status(OPEN)
                .openedDate(LocalDateTime.now())
                .team(getActiveTeam())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private PositionEntity getOpenPositionEntity3() {
        return PositionEntity.builder()
                .id(3L)
                .name("Sample position 3")
                .note("A sample position 3")
                .quantity(8)
                .status(OPEN)
                .openedDate(LocalDateTime.now())
                .team(getActiveTeam())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private PositionEntity getOpenPositionEntityWithOpenedDate() {
        LocalDateTime openedDateFormat = LocalDateTime.of(2023, 9, 12, 19, 9, 00);
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position")
                .note("A sample position")
                .quantity(8)
                .status(OPEN)
                .team(getActiveTeam())
                .openedDate(openedDateFormat)
                .createdDate(openedDateFormat)
                .build();
    }

    private PositionEntity getOpenPositionEntityWithoutInputOpenedDate() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position")
                .note("A sample position")
                .quantity(8)
                .status(OPEN)
                .team(getActiveTeam())
                .createdDate(LocalDateTime.now())
                .openedDate(LocalDateTime.now())
                .build();
    }

    private PositionEntity getClosePositionEntity() {
        return PositionEntity.builder()
                .id(1L)
                .name("Sample position")
                .quantity(8)
                .openedDate(LocalDateTime.now())
                .status(CLOSE)
                .build();
    }

    private Position getOpenPositionDTO() {
        return Position.builder()
                .id(1L)
                .name("Sample position")
                .note("A sample position")
                .quantity(8)
                .status(OPEN)
                .teamId(1L)
                .build();
    }

    private Position getOpenPositionDTO1() {
        return Position.builder()
                .id(1L)
                .name("Sample position 1")
                .note("A sample position 1")
                .quantity(8)
                .status(OPEN)
                .teamId(1L)
                .build();
    }

    private Position getClosePositionDTO() {
        return Position.builder()
                .id(1L)
                .name("Sample position")
                .quantity(8)
                .status(CLOSE)
                .build();
    }

    private Position getPositionWithName(String name) {
        return Position.builder()
                .id(1L)
                .name(name)
                .quantity(10)
                .status(OPEN)
                .teamId(1L)
                .build();
    }

    private Position getDuplicatedNamePosition() {
        return Position.builder()
                .name("Sample position")
                .quantity(10)
                .status(OPEN)
                .teamId(1L)
                .build();
    }

    private Position getPositionWithNote(String note) {
        return Position.builder()
                .name("Sample position")
                .note(note)
                .quantity(10)
                .status(OPEN)
                .build();
    }

    private Position getPositionWithQuantity(Integer quantity) {
        return Position.builder()
                .name("Sample position")
                .quantity(quantity)
                .status(OPEN)
                .build();
    }

    private List<PositionEntity> getPositionList() {
        return Arrays.asList(getOpenPositionEntity(), getClosePositionEntity());
    }

    private List<PositionEntity> getListOfPositionEntity() {
        return Arrays.asList(getOpenPositionEntity(), getOpenPositionEntity(), getClosePositionEntity());
    }

    private List<PositionEntity> getListOfOpenPositionEntity() {
        return Arrays.asList(getOpenPositionEntity(), getOpenPositionEntity());
    }

    private List<PositionEntity> getListOfOpenPositionEntity2() {
        return Arrays.asList(getOpenPositionEntity(), getOpenPositionEntity3());
    }

    private List<Position> getListOfPositionDTO() {
        return Arrays.asList(getOpenPositionDTO(), getOpenPositionDTO(), getClosePositionDTO());
    }

    private List<Position> getListOfOpenPositionDTO() {
        return Arrays.asList(getOpenPositionDTO(), getOpenPositionDTO());
    }
}