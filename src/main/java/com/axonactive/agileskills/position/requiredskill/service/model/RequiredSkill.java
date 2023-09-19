package com.axonactive.agileskills.position.requiredskill.service.model;

import com.axonactive.agileskills.position.requiredskill.entity.LevelEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequireEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import java.util.List;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_REQUIRED_SKILL_NOTE_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NOTE;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequiredSkill {

    private Long id;
    private Long skillId;
    private String skillName;
    private String skillDescription;
    private Long positionId;

    @Enumerated(EnumType.STRING)
    private RequireEnum require;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Size(max = MAX_SIZE_NOTE, message = KEY_REQUIRED_SKILL_NOTE_LENGTH_CONSTRAINT)
    private String note;

    private List<RequiredTopic> requiredTopicList;
}
