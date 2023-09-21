package com.axonactive.agileskills.skill.topic.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.skill.dao.SkillDAO;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.topic.dao.TopicDAO;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import com.axonactive.agileskills.skill.topic.service.mapper.TopicMapper;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TopicServiceTest {

    @Mock
    private TopicDAO topicDAO;

    @Mock
    private SkillDAO skillDAO;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private TopicService topicService;

    @Test
    void createTopic_MandatoryFields_ReturnModelWithSameFields() throws InputValidationException, ResourceNotFoundException {
        SkillEntity skill = getSkillEntity();
        TopicEntity topic = getTopic1(skill);

        Topic topicDTO = Topic.builder()
                .id(1L)
                .name("Java 8")
                .description("Java 8 is fun")
                .status(StatusEnum.ACTIVE)
                .skillId(1L)
                .build();

        Topic createdTopic = getCreatedTopic();

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topic);
        when(topicMapper.toDTO(topic)).thenReturn(topicDTO);

        Topic returnTopic = topicService.create(createdTopic, 1L);

        assertEquals(1, returnTopic.getId());
        assertEquals("Java 8", returnTopic.getName());
        assertEquals("Java 8 is fun", returnTopic.getDescription());
        assertEquals(StatusEnum.ACTIVE, returnTopic.getStatus());
        assertEquals(1, returnTopic.getSkillId());
    }


    @Test
    void createTopic_DuplicateName_ThrowException() {
        SkillEntity skill = getActiveSkillEntity();

        TopicEntity topic1 = getTopic1(skill);

        TopicEntity topic2 = getTopic2Entity(skill);

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.findBySkillIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Arrays.asList(topic1, topic2));

        Topic createdTopic = getCreatedTopic();

        assertThrows(InputValidationException.class, () -> topicService.create(createdTopic, 1L));
    }

    @Test
    void createTopic_DuplicateNameDifferentCase_ThrowException() {
        SkillEntity skill = getActiveSkillEntity();

        TopicEntity topic1 = getTopic1(skill);

        TopicEntity topic2 = getTopic2Entity(skill);

        topic2.setName(topic1.getName().toUpperCase());

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.findBySkillIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Arrays.asList(topic1, topic2));

        Topic createdTopic = getCreatedTopic();

        assertThrows(InputValidationException.class, () -> topicService.create(createdTopic, 1L));
    }

    @Test
    void createTopic_NullName_ThrowException() {
        Topic topic = getTopicWithName(null);
        assertThrows(ConstraintViolationException.class, () -> topicService.create(topic, 1L));
    }

    @Test
    void createTopic_BlankName_ThrowException() {
        Topic topic = getTopicWithName("   ");
        assertThrows(ConstraintViolationException.class, () -> topicService.create(topic, 1L));
    }

    @Test
    void createTopic_EmptyName_ThrowException() {
        Topic topic = getTopicWithName("");
        assertThrows(ConstraintViolationException.class, () -> topicService.create(topic, 1L));
    }

    @Test
    void createTopic_SkillIdNotExisted_ThrowException() {
        when(skillDAO.findByIdAndStatus(999L, StatusEnum.ACTIVE)).thenReturn(Optional.empty());
        Topic createdTopic = Topic.builder()
                .name("Java 17")
                .description("Java 17 is fun")
                .build();

        assertThrows(ResourceNotFoundException.class, () -> topicService.create(createdTopic, 999L));
    }

    @Test
    void createTopic_NameLengthLessThan3Chars_ThrowException() {
        Topic createdTopic = Topic.builder()
                .name("Ja")
                .description("Ja Ja Ja")
                .build();

        assertThrows(ConstraintViolationException.class, () -> topicService.create(createdTopic, 1L));
    }

    @Test
    void createTopic_NameLengthEquals3Chars_ReturnModelWithSameFields() throws InputValidationException, ResourceNotFoundException {
        String topicNameCharacters = "C++";

        SkillEntity skill = getSkillEntity();
        TopicEntity topic = getTopic1(skill);

        topic.setName(topicNameCharacters);

        Topic topicDTO = Topic.builder()
                .id(1L)
                .name(topicNameCharacters)
                .description("Java 8 is fun")
                .status(StatusEnum.ACTIVE)
                .skillId(1L)
                .build();

        Topic createdTopic = getCreatedTopic();

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topic);
        when(topicMapper.toDTO(topic)).thenReturn(topicDTO);

        Topic returnTopic = topicService.create(createdTopic, 1L);

        assertEquals(1, returnTopic.getId());
        assertEquals("C++", returnTopic.getName());
        assertEquals("Java 8 is fun", returnTopic.getDescription());
        assertEquals(StatusEnum.ACTIVE, returnTopic.getStatus());
        assertEquals(1, returnTopic.getSkillId());
    }

    @Test
    void createTopic_NameLengthEquals255Chars_ReturnModelWithSameFields() throws InputValidationException, ResourceNotFoundException {
        String topicNameCharacters = "a".repeat(255);

        SkillEntity skill = getSkillEntity();
        TopicEntity topic = getTopic1(skill);

        topic.setName(topicNameCharacters);

        Topic topicDTO = Topic.builder()
                .id(1L)
                .name(topicNameCharacters)
                .description("Java 8 is fun")
                .status(StatusEnum.ACTIVE)
                .skillId(1L)
                .build();

        Topic createdTopic = getCreatedTopic();

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topic);
        when(topicMapper.toDTO(topic)).thenReturn(topicDTO);

        Topic returnTopic = topicService.create(createdTopic, 1L);

        assertEquals(1, returnTopic.getId());
        assertEquals("a".repeat(255), returnTopic.getName());
        assertEquals("Java 8 is fun", returnTopic.getDescription());
        assertEquals(StatusEnum.ACTIVE, returnTopic.getStatus());
        assertEquals(1, returnTopic.getSkillId());
    }

    @Test
    void createTopic_NameLengthOver255Chars_ThrowException() {
        Topic createdTopic = Topic.builder()
                .name("a".repeat(256))
                .description("Java 8 is fun")
                .build();

        assertThrows(ConstraintViolationException.class, () -> topicService.create(createdTopic, 1L));
    }

    @Test
    void createTopic_DescriptionLengthEquals2000Chars_ReturnModelWithSameFields() throws InputValidationException, ResourceNotFoundException {
        String topicDescriptionCharacters = "a".repeat(2000);

        SkillEntity skill = getSkillEntity();
        TopicEntity topic = getTopic1(skill);

        topic.setDescription(topicDescriptionCharacters);

        Topic topicDTO = Topic.builder()
                .id(1L)
                .name("Java 8")
                .description(topicDescriptionCharacters)
                .status(StatusEnum.ACTIVE)
                .skillId(1L)
                .build();

        Topic createdTopic = getCreatedTopic();

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.create(any(TopicEntity.class))).thenReturn(topic);
        when(topicMapper.toDTO(topic)).thenReturn(topicDTO);

        Topic returnTopic = topicService.create(createdTopic, 1L);

        assertEquals(1, returnTopic.getId());
        assertEquals("Java 8", returnTopic.getName());
        assertEquals("a".repeat(2000), returnTopic.getDescription());
        assertEquals(StatusEnum.ACTIVE, returnTopic.getStatus());
        assertEquals(1, returnTopic.getSkillId());
    }

    @Test
    void createTopic_DescriptionOver2000Chars_ThrowException() {
        Topic createdTopic = Topic.builder()
                .name("Java 20")
                .description("a".repeat(2001))
                .build();

        assertThrows(ConstraintViolationException.class, () -> topicService.create(createdTopic, 1L));
    }

    @Test
    void createTopic_InactiveSkill_ThrowException() {
        SkillEntity skill = SkillEntity.builder()
                .id(1L)
                .name("Java")
                .description("Java is fun")
                .status(StatusEnum.INACTIVE)
                .build();

        Topic createdTopic = getCreatedTopic();

        assertThrows(ResourceNotFoundException.class, () -> topicService.create(createdTopic, skill.getId()));

    }

    @Test
    void getTopicList_SkillIdExisting_ReturnTopicModelList() throws ResourceNotFoundException {
        SkillEntity skill = getSkillEntity();

        TopicEntity topic1 = getTopic1Entity(skill);

        TopicEntity topic2 = getTopic2Entity(skill);

        Topic TopicDTO1 = getTopicDTO1();

        Topic TopicDTO2 = getTopicDTO2();

        List<TopicEntity> topicList = Arrays.asList(topic1, topic2);
        List<Topic> topicDTOList = Arrays.asList(TopicDTO1, TopicDTO2);

        when(skillDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(skill));
        when(topicDAO.findBySkillIdAndStatus(skill.getId(), StatusEnum.ACTIVE)).thenReturn(topicList);
        when(topicMapper.toDTOList(topicList)).thenReturn(topicDTOList);

        assertEquals(2, topicService.getBySkillIdAndStatus(1L, StatusEnum.ACTIVE).size());
    }

    @Test
    void getTopicList_SkillIdNotExisted_ThrowException() {
        when(skillDAO.findByIdAndStatus(999L, StatusEnum.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> topicService.getBySkillIdAndStatus(999L, StatusEnum.ACTIVE));
    }


    @Test
    void softDelete_TopicIdExisting_ReturnModelWithPrefixName() throws ResourceNotFoundException {
        SkillEntity skill = getSkillEntity();

        TopicEntity topic = TopicEntity.builder()
                .id(1L)
                .name("INACTIVE_16ChRandomString_Java 8")
                .description("Java 8 is fun")
                .status(StatusEnum.INACTIVE)
                .skill(skill)
                .build();

        Topic topicDTO = Topic.builder()
                .id(1L)
                .name("INACTIVE_16ChRandomString_Java 8")
                .description("Java 8 is fun")
                .status(StatusEnum.INACTIVE)
                .skillId(1L)
                .build();

        when(topicDAO.softDelete(1L)).thenReturn(topic);
        when(topicMapper.toDTO(topic)).thenReturn(topicDTO);

        Topic deletedTopic = topicService.softDelete(1L);

        assertEquals(1, deletedTopic.getId());
        assertEquals("INACTIVE_16ChRandomString_Java 8", deletedTopic.getName());
        assertEquals("Java 8 is fun", deletedTopic.getDescription());
        assertEquals(StatusEnum.INACTIVE, deletedTopic.getStatus());
        assertEquals(1, deletedTopic.getSkillId());
    }

    @Test
    void softDelete_NonExistingTopic_ThrowException() {
        when(topicDAO.softDelete(1L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> topicService.softDelete(1L));
    }

    private Topic getTopicWithName(String name) {
        return Topic.builder()
                .name(name)
                .description("Java 8 is fun")
                .status(StatusEnum.ACTIVE)
                .build();
    }

    private SkillEntity getSkillEntity() {
        return SkillEntity.builder()
                .name("Java")
                .description("Java is fun")
                .status(StatusEnum.ACTIVE)
                .build();
    }

    private Topic getTopicDTO2() {
        return Topic.builder()
                .name("Java 9")
                .description("Java 9 is fun")
                .skillId(1L)
                .build();
    }

    private Topic getTopicDTO1() {
        return Topic.builder()
                .name("Java 8")
                .description("Java 8 is fun")
                .skillId(1L)
                .build();
    }

    private TopicEntity getTopic2Entity(SkillEntity skill) {
        return TopicEntity.builder()
                .name("Java 9")
                .description("Java 9 is fun")
                .status(StatusEnum.ACTIVE)
                .skill(skill)
                .build();
    }

    private TopicEntity getTopic1Entity(SkillEntity skill) {
        return TopicEntity.builder()
                .name("Java 8")
                .description("Java 8 is fun")
                .status(StatusEnum.ACTIVE)
                .skill(skill)
                .build();
    }

    private Topic getCreatedTopic() {
        return Topic.builder()
                .id(1L)
                .name("Java 8")
                .description("Java 8 is fun")
                .build();
    }

    private TopicEntity getTopic1(SkillEntity skill) {
        return getTopic1Entity(skill);
    }

    private SkillEntity getActiveSkillEntity() {
        return getSkillEntity();
    }
}