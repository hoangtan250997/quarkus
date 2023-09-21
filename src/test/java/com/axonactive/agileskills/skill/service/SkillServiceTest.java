package com.axonactive.agileskills.skill.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.skill.dao.SkillDAO;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.service.mapper.SkillMapper;
import com.axonactive.agileskills.skill.service.model.Skill;
import com.axonactive.agileskills.skill.topic.dao.TopicDAO;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import com.axonactive.agileskills.skill.topic.service.TopicService;
import com.axonactive.agileskills.skill.topic.service.mapper.TopicMapper;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import jakarta.validation.ConstraintViolationException;
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
import static com.axonactive.agileskills.base.entity.StatusEnum.INACTIVE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.SKILL_ALREADY_EXISTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class SkillServiceTest {

    @InjectMocks
    private SkillService skillService;

    @Mock
    private SkillDAO skillDAO;
    @Mock
    private TopicDAO topicDAO;
    @Mock
    private SkillMapper skillMapper;
    @Mock
    private TopicMapper topicMapper;

    @Mock
    private TopicService topicService;

    @Mock
    private SkillListCache skillListCache;

    @Test
    void createSkill_FullFields_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        Skill skill = getSkill(topic1, topic2);

        SkillEntity skillEntity = getSkillEntity();

        TopicEntity topicEntity1 = getTopicEntity(1L, "Topic 1", skillEntity);
        TopicEntity topicEntity2 = getTopicEntity(2L, "Topic 2", skillEntity);

        Skill returnSkill = getReturnSkill(topic1, topic2, skill);
        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topicEntity1, topicEntity2);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(returnSkill);
        when(topicMapper.toDTOList(anyList())).thenReturn(Arrays.asList(topic1, topic2));

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(skillEntity.getName(), returnedSkill.getName());
        assertEquals(skillEntity.getDescription(), returnedSkill.getDescription());
        assertEquals(ACTIVE, returnedSkill.getStatus());
        assertEquals(skill.getTopicList().size(), returnedSkill.getTopicList().size());
        assertEquals(topic1.getName(), returnedSkill.getTopicList().get(0).getName());
        assertEquals(topic1.getDescription(), returnedSkill.getTopicList().get(0).getDescription());
        assertEquals(topic2.getName(), returnedSkill.getTopicList().get(1).getName());
        assertEquals(topic2.getDescription(), returnedSkill.getTopicList().get(1).getDescription());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_ValidSkillNameWithOneCharacter_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        String validSkillName = "C";
        Skill skill = getSkill(topic1, topic2);
        skill.setName(validSkillName);

        SkillEntity skillEntity = getSkillEntity();

        TopicEntity topicEntity1 = getTopicEntity(1L, "Topic 1", skillEntity);
        TopicEntity topicEntity2 = getTopicEntity(2L, "Topic 2", skillEntity);

        Skill returnSkill = getReturnSkill(topic1, topic2, skill);

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topicEntity1, topicEntity2);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(returnSkill);
        when(topicMapper.toDTOList(anyList())).thenReturn(Arrays.asList(topic1, topic2));

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(validSkillName, returnedSkill.getName());
        assertEquals(skill.getDescription(), returnedSkill.getDescription());
        assertEquals(StatusEnum.ACTIVE, returnedSkill.getStatus());
        assertEquals(2, returnedSkill.getTopicList().size());
        assertEquals(topic1.getName(), returnedSkill.getTopicList().get(0).getName());
        assertEquals(topic1.getDescription(), returnedSkill.getTopicList().get(0).getDescription());
        assertEquals(topic2.getName(), returnedSkill.getTopicList().get(1).getName());
        assertEquals(topic2.getDescription(), returnedSkill.getTopicList().get(1).getDescription());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_ValidSkillNameWithSpecialCharacter_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        String validSkillName = "C++";
        Skill skill = getSkill(topic1, topic2);
        skill.setName(validSkillName);

        SkillEntity skillEntity = getSkillEntity();

        TopicEntity topicEntity1 = getTopicEntity(1L, "Topic 1", skillEntity);
        TopicEntity topicEntity2 = getTopicEntity(2L, "Topic 2", skillEntity);

        Skill returnSkill = getReturnSkill(topic1, topic2, skill);

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topicEntity1, topicEntity2);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(returnSkill);
        when(topicMapper.toDTOList(anyList())).thenReturn(Arrays.asList(topic1, topic2));

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(validSkillName, returnedSkill.getName());
        assertEquals(skill.getDescription(), returnedSkill.getDescription());
        assertEquals(ACTIVE, returnedSkill.getStatus());
        assertEquals(2, returnedSkill.getTopicList().size());
        assertEquals(topic1.getName(), returnedSkill.getTopicList().get(0).getName());
        assertEquals(topic1.getDescription(), returnedSkill.getTopicList().get(0).getDescription());
        assertEquals(topic2.getName(), returnedSkill.getTopicList().get(1).getName());
        assertEquals(topic2.getDescription(), returnedSkill.getTopicList().get(1).getDescription());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_ValidSkillName255Characters_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        String validSkillName = "C".repeat(255);

        Skill skill = getSkill(topic1, topic2);
        skill.setName(validSkillName);

        SkillEntity skillEntity = getSkillEntity();

        TopicEntity topicEntity1 = getTopicEntity(1L, "Topic 1", skillEntity);
        TopicEntity topicEntity2 = getTopicEntity(2L, "Topic 2", skillEntity);

        Skill returnSkill = getReturnSkill(topic1, topic2, skill);

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topicEntity1, topicEntity2);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(returnSkill);
        when(topicMapper.toDTOList(anyList())).thenReturn(Arrays.asList(topic1, topic2));

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(validSkillName, returnedSkill.getName());
        assertEquals(skill.getDescription(), returnedSkill.getDescription());
        assertEquals(ACTIVE, returnedSkill.getStatus());
        assertEquals(2, returnedSkill.getTopicList().size());
        assertEquals(topic1.getName(), returnedSkill.getTopicList().get(0).getName());
        assertEquals(topic1.getDescription(), returnedSkill.getTopicList().get(0).getDescription());
        assertEquals(topic2.getName(), returnedSkill.getTopicList().get(1).getName());
        assertEquals(topic2.getDescription(), returnedSkill.getTopicList().get(1).getDescription());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_NameIsNull_ThrowException() {
        Skill createdSkill = Skill.builder()
                .name(null)
                .description("Some skill description")
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(createdSkill));
    }

    @Test
    void createSkill_NameOver255Chars_ThrowException() {
        Skill createdSkill = Skill.builder()
                .name("a".repeat(256))
                .description("- Some skill description")
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(createdSkill));
    }

    @Test
    void createSkill_TopicListIsNull_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .build();

        SkillEntity skillEntity = getSkillEntity();

        Skill mockReturnSkill = Skill.builder()
                .id(1L)
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .build();

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(mockReturnSkill);

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(skillEntity.getName(), returnedSkill.getName());
        assertEquals(skillEntity.getDescription(), returnedSkill.getDescription());
        assertEquals(skillEntity.getStatus(), returnedSkill.getStatus());
        assertEquals(0, returnedSkill.getTopicList().size());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_TopicListIsEmpty_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(new ArrayList<>())
                .build();

        SkillEntity skillEntity = getSkillEntity();
        Skill returnSkill = Skill.builder()
                .id(1L)
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .build();

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(returnSkill);

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(skillEntity.getName(), returnedSkill.getName());
        assertEquals(skillEntity.getDescription(), returnedSkill.getDescription());
        assertEquals(skillEntity.getStatus(), returnedSkill.getStatus());
        assertEquals(0, returnedSkill.getTopicList().size());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_OnlyOneInvalidTopic_ThrowException() {
        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(List.of(
                        new Topic()
                ))
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(skill));
    }

    @Test
    void createSkill_OneInvalidTopic_ThrowException() {
        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(Arrays.asList(
                        topic1,
                        topic2,
                        new Topic()
                ))
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(skill));
    }

    @Test
    void createSkill_NullTopicName_ThrowException() {
        Topic topic = Topic.builder()
                .description("Topic 1 description")
                .build();

        SkillEntity skillEntity = getSkillEntity();

        Skill createdSkill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(Collections.singletonList(topic))
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(createdSkill));
    }

    @Test
    void createSkill_DuplicatedTopicNames_ThrowException() {
        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 1");

        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(Arrays.asList(topic1, topic2))
                .build();

        assertThrows(InputValidationException.class, () -> skillService.create(skill));
    }

    @Test
    void createSkill_DuplicatedTopicNamesDifferentCase_ThrowException() {
        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 1");
        topic2.setName("tOpIc 1");

        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(StatusEnum.ACTIVE)
                .topicList(Arrays.asList(topic1, topic2))
                .build();

        assertThrows(InputValidationException.class, () -> skillService.create(skill));
    }

    @Test
    void createSkill_WithTopicListButSkillNameIsNull_ThrowException() {
        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        Skill skill = getSkill(topic1, topic2);
        skill.setName(null);

        assertThrows(ConstraintViolationException.class, () -> skillService.create(skill));
    }

    @Test
    void createSkill_DuplicateName_ThrowException() {
        SkillEntity skillEntity = SkillEntity.builder()
                .description("Java is fun")
                .name("Java")
                .status(ACTIVE)
                .build();

        Skill inputSkill = Skill.builder()
                .name("Java")
                .description("Java is fun")
                .build();

        when(skillDAO.findByName(inputSkill.getName().trim().toLowerCase()))
                .thenReturn(Optional.ofNullable(skillEntity));

        assertThrows(InputValidationException.class, () -> skillService.create(inputSkill), SKILL_ALREADY_EXISTED);
    }

    @Test
    void createSkill_DuplicateNameDifferentCase_ThrowException() {
        SkillEntity skillEntity = SkillEntity.builder()
                .description("Java is fun")
                .name("Java")
                .status(StatusEnum.ACTIVE)
                .build();

        Skill inputSkill = Skill.builder()
                .name("jAvA")
                .description("Java is fun")
                .build();

        when(skillDAO.findByName(inputSkill.getName().trim().toLowerCase()))
                .thenReturn(Optional.of(skillEntity));

        assertThrows(InputValidationException.class, () -> skillService.create(inputSkill), SKILL_ALREADY_EXISTED);
    }

    @Test
    void createSkill_SkillDescriptionOver2000Chars_ThrowException() {
        Skill createdSkill = Skill.builder()
                .name("Some skill name")
                .description("a".repeat(2001))
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(createdSkill));
    }

    @Test
    void createSkill_SkillDescriptionEquals2000Chars_ReturnModelWithSameFields() throws InputValidationException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        Topic topic1 = getTopic("Topic 1");
        Topic topic2 = getTopic("Topic 2");

        Skill skill = getSkill(topic1, topic2);
        skill.setDescription("a".repeat(2000));

        SkillEntity skillEntity = getSkillEntity();
        skillEntity.setDescription("a".repeat(2000));

        TopicEntity topicEntity1 = getTopicEntity(1L, "Topic 1", skillEntity);

        TopicEntity topicEntity2 = getTopicEntity(2L, "Topic 2", skillEntity);

        Skill returnSkill = Skill.builder()
                .id(1L)
                .name("Java")
                .status(ACTIVE)
                .description(skill.getDescription())
                .topicList(Arrays.asList(
                        topic1,
                        topic2
                ))
                .build();

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.create(any(SkillEntity.class))).thenReturn(skillEntity);
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topicEntity1, topicEntity2);
        when(skillMapper.toDTO(any(SkillEntity.class))).thenReturn(returnSkill);
        when(topicMapper.toDTOList(anyList())).thenReturn(Arrays.asList(topic1, topic2));

        Skill returnedSkill = skillService.create(skill);

        assertEquals(skillEntity.getId(), returnedSkill.getId());
        assertEquals(skillEntity.getName(), returnedSkill.getName());
        assertEquals(skillEntity.getDescription(), returnedSkill.getDescription());
        assertEquals(skillEntity.getStatus(), returnedSkill.getStatus());
        assertEquals(2, returnedSkill.getTopicList().size());
        assertEquals(topic1.getName(), returnedSkill.getTopicList().get(0).getName());
        assertEquals(topic1.getDescription(), returnedSkill.getTopicList().get(0).getDescription());
        assertEquals(topic2.getName(), returnedSkill.getTopicList().get(1).getName());
        assertEquals(topic2.getDescription(), returnedSkill.getTopicList().get(1).getDescription());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void createSkill_NameIsBlank_ThrowException() {
        Skill createdSkill = Skill.builder()
                .name(" ")
                .description("- Some skill description")
                .build();

        assertThrows(ConstraintViolationException.class, () -> skillService.create(createdSkill));
    }

    @Test
    void getByStatus_ActiveSkillWithCacheAvailable_ReturnModelSkillList() {
        List<SkillEntity> skillEntityList = getActiveSkillEntityList();
        List<Skill> skillList = getActiveSkillDTOList();

        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", skillEntityList);

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillMapper.toDTOList(skillEntityList)).thenReturn(skillList);

        List<Skill> actualSkillList = skillService.getByStatus(ACTIVE);

        assertEquals(getActiveSkillDTOList().size(), actualSkillList.size());
    }

    @Test
    void getByStatus_ActiveSkillWithCacheUnavailable_ReturnModelSkillList() {
        List<SkillEntity> skillEntityList = getActiveSkillEntityList();
        List<Skill> skillList = getActiveSkillDTOList();

        SkillListCache skillListCache1 = new SkillListCache();

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.findByStatus(ACTIVE)).thenReturn(skillEntityList);
        when(skillMapper.toDTOList(skillEntityList)).thenReturn(skillList);

        List<Skill> actualSkillList = skillService.getByStatus(ACTIVE);

        assertEquals(getActiveSkillDTOList().size(), actualSkillList.size());
    }

    @Test
    void getByStatus_InactiveSkillWithCacheAvailable_ReturnModelSkillList() {
        List<SkillEntity> inactiveSkillEntityList = getInactiveSkillEntityList();
        List<Skill> inactiveSkillList = getInactiveSkillList();

        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("INACTIVE", inactiveSkillEntityList);

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillMapper.toDTOList(inactiveSkillEntityList)).thenReturn(inactiveSkillList);

        assertEquals(inactiveSkillEntityList.size(), skillService.getByStatus(INACTIVE).size());
    }

    @Test
    void getByStatus_InactiveSkillWithCacheUnAvailable_ReturnModelSkillList() {
        List<SkillEntity> inactiveSkillEntityList = getInactiveSkillEntityList();
        List<Skill> inactiveSkillList = getInactiveSkillList();

        SkillListCache skillListCache1 = new SkillListCache();

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.findByStatus(INACTIVE)).thenReturn(inactiveSkillEntityList);
        when(skillMapper.toDTOList(inactiveSkillEntityList)).thenReturn(inactiveSkillList);

        assertEquals(inactiveSkillEntityList.size(), skillService.getByStatus(INACTIVE).size());
    }

    @Test
    void getByIdAndStatus_ActiveSkill_ReturnModelSkillList() throws ResourceNotFoundException {
        SkillEntity javaSkillEntity = getSkillEntity();

        Skill javaSkillDTO = Skill.builder()
                .name(javaSkillEntity.getName())
                .description(javaSkillEntity.getDescription())
                .build();

        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(javaSkillEntity));
        when(skillMapper.toDTO(javaSkillEntity)).thenReturn(javaSkillDTO);

        Skill actualSkillList = skillService.getByIdAndStatus(1L, ACTIVE);

        assertEquals(javaSkillDTO, actualSkillList);
    }

    @Test
    void getByIdAndStatus_NonExistingSkill_ReturnModelSkillList() {
        when(skillDAO.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> skillService.getByIdAndStatus(1L, ACTIVE));
    }


    @Test
    void softDelete_IdExisted_ReturnModelWithPrefixName() throws ResourceNotFoundException {
        SkillListCache skillListCache1 = new SkillListCache();
        skillListCache1.getCache().put("ACTIVE", getActiveSkillEntityList());

        SkillEntity skillEntity = SkillEntity.builder()
                .id(1L)
                .description("Assembly is fun")
                .name("INACTIVE1689682070456_Assembly")
                .status(INACTIVE)
                .build();

        Skill skill = Skill.builder()
                .id(1L)
                .description("Assembly is fun")
                .name("INACTIVE1689682070456_Assembly")
                .status(INACTIVE)
                .build();

        when(skillListCache.getCache()).thenReturn(skillListCache1.getCache());
        when(skillDAO.softDelete(1L)).thenReturn(skillEntity);
        when(skillMapper.toDTO(skillEntity)).thenReturn(skill);

        Skill deletedSkill = skillService.softDelete(1L);

        assertEquals(1, deletedSkill.getId());
        assertEquals("INACTIVE1689682070456_Assembly", deletedSkill.getName());
        assertEquals("Assembly is fun", deletedSkill.getDescription());
        assertEquals(INACTIVE, deletedSkill.getStatus());

        verify(skillListCache).getCache();
        assertNull(skillListCache.getCache().getIfPresent("ACTIVE"));
        assertEquals(0, skillListCache.getCache().estimatedSize());
    }

    @Test
    void softDelete_NonExistingSkill_ThrowException() {
        when(skillDAO.softDelete(1L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> skillService.softDelete(1L));
    }

    @Test
    void getSkillListWithTopicList_ExistedSkill_ReturnSkillWithTopicList() throws ResourceNotFoundException {
        SkillEntity firstSkillEntity = getSkillEntity();

        TopicEntity topicOneEntity = getTopicEntity(1L, "Topic 1", firstSkillEntity);
        TopicEntity topicTwoEntity = getTopicEntity(2L, "Topic 2", firstSkillEntity);

        SkillEntity secondSkillEntity = getSkillEntity();
        secondSkillEntity.setId(2L);
        secondSkillEntity.setName("C++");

        TopicEntity topicThreeEntity = getTopicEntity(3L, "Topic 3", secondSkillEntity);
        TopicEntity topicFourEntity = getTopicEntity(4L, "Topic 4", secondSkillEntity);

        List<SkillEntity> skillEntityList = List.of(firstSkillEntity, secondSkillEntity);

        Topic topicOne = getTopic("Topic 1");
        Topic topicTwo = getTopic("Topic 2");
        Topic topicThree = getTopic("Topic 3");
        Topic topicFour = getTopic("Topic 4");

        Skill firstSkill = getSkill(topicOne, topicTwo);
        Skill secondSkill = getSkill(topicThree, topicFour);

        List<Topic> topicListOfFirstSkill = firstSkill.getTopicList();
        List<Topic> topicListOfSecondSkill = secondSkill.getTopicList();

        List<Skill> skillList = List.of(firstSkill, secondSkill);

        when(skillDAO.findByStatus(ACTIVE)).thenReturn(skillEntityList);
        when(skillMapper.toDTOList(skillEntityList)).thenReturn(skillList);
        when(topicService.getBySkillIdAndStatus(firstSkill.getId(), ACTIVE)).thenReturn(topicListOfFirstSkill);
        when(topicService.getBySkillIdAndStatus(secondSkill.getId(), ACTIVE)).thenReturn(topicListOfSecondSkill);

        List<Skill> actualSkillList = skillService.getSkillListIncludingTopicList();

        assertEquals(skillList, actualSkillList);
        assertEquals(skillList.size(), actualSkillList.size());
        assertEquals(topicListOfFirstSkill.size(), actualSkillList.get(0).getTopicList().size());
    }

    private Skill getSkill(Topic topic1, Topic topic2) {
        Skill skill = Skill.builder()
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(Arrays.asList(
                        topic1,
                        topic2
                ))
                .build();
        return skill;
    }

    private TopicEntity getTopicEntity(Long id, String name, SkillEntity skillEntity) {
        TopicEntity topicEntity1 = TopicEntity.builder()
                .id(id)
                .name(name)
                .description("Topic description")
                .status(ACTIVE)
                .skill(skillEntity)
                .build();
        return topicEntity1;
    }

    private Topic getTopic(String topicName) {
        Topic topic1 = Topic.builder()
                .name(topicName)
                .description("Topic description")
                .build();
        return topic1;
    }

    private SkillEntity getSkillEntity() {
        SkillEntity skillEntity = SkillEntity.builder()
                .id(1L)
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .build();
        return skillEntity;
    }

    private SkillEntity getSkillEntity1() {
        SkillEntity skillEntity = SkillEntity.builder()
                .id(2L)
                .name("Python")
                .description("Python programming language")
                .status(ACTIVE)
                .build();
        return skillEntity;
    }

    private Skill getSkillDTO() {
        return Skill.builder()
                .id(1L)
                .name("Java")
                .description("Java programming language")
                .status(ACTIVE)
                .build();
    }

    private Skill getSkillDTO1() {
        return Skill.builder()
                .id(2L)
                .name("Python")
                .description("Python programming language")
                .status(ACTIVE)
                .build();
    }

    private List<Skill> getActiveSkillDTOList() {
        return Arrays.asList(getSkillDTO(), getSkillDTO1());
    }

    private List<SkillEntity> getActiveSkillEntityList() {
        return Arrays.asList(getSkillEntity(), getSkillEntity1());
    }

    private SkillEntity getInactiveSkillEntity1() {
        return SkillEntity.builder()
                .id(1L)
                .name("Java")
                .description("Java programming language")
                .status(INACTIVE)
                .build();
    }

    private SkillEntity getInactiveSkillEntity2() {
        return SkillEntity.builder()
                .id(1L)
                .name("Python")
                .description("Python programming language")
                .status(INACTIVE)
                .build();
    }

    private List<SkillEntity> getInactiveSkillEntityList() {
        return Arrays.asList(getInactiveSkillEntity1(), getInactiveSkillEntity2());
    }

    private List<Skill> getInactiveSkillList() {
        return Arrays.asList(getInactiveSkill1(), getInactiveSkill2());
    }

    private Skill getInactiveSkill1() {
        return Skill.builder()
                .id(1L)
                .name("Java")
                .description("Java programming language")
                .status(INACTIVE)
                .build();
    }

    private Skill getInactiveSkill2() {
        return Skill.builder()
                .id(1L)
                .name("Python")
                .description("Python programming language")
                .status(INACTIVE)
                .build();
    }

    private Skill getReturnSkill(Topic topic1, Topic topic2, Skill skill) {
        Skill returnSkill = Skill.builder()
                .id(1L)
                .name(skill.getName())
                .description("Java programming language")
                .status(ACTIVE)
                .topicList(Arrays.asList(
                        topic1,
                        topic2
                ))
                .build();
        return returnSkill;
    }
}