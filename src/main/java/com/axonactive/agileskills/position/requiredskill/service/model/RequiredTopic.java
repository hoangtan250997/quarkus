package com.axonactive.agileskills.position.requiredskill.service.model;

import com.axonactive.agileskills.position.requiredskill.entity.LevelEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequireEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_REQUIRED_TOPIC_NOTE_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NOTE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequiredTopic {

    private Long id;
    private Long topicId;
    private String topicName;
    private String topicDescription;
    private Long requiredSkillId;

    @Enumerated(EnumType.STRING)
    private RequireEnum require;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Size(max = MAX_SIZE_NOTE, message = KEY_REQUIRED_TOPIC_NOTE_LENGTH_CONSTRAINT)
    private String note;
}
