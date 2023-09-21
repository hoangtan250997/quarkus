package com.axonactive.agileskills.skill.service.model;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_DESCRIPTION_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_NAME_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_SKILL_NAME_NULL_OR_BLANK;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_DESCRIPTION;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NAME;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Skill {


    private Long id;

    @NotBlank(message = KEY_SKILL_NAME_NULL_OR_BLANK)
    @Size(max = MAX_SIZE_NAME, message = KEY_SKILL_NAME_LENGTH_CONSTRAINT)
    private String name;

    @Size(max = MAX_SIZE_DESCRIPTION, message = KEY_SKILL_DESCRIPTION_LENGTH_CONSTRAINT)

    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private List<Topic> topicList;
}
