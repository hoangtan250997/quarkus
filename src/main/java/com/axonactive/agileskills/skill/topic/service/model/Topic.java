package com.axonactive.agileskills.skill.topic.service.model;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_DESCRIPTION_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_NAME_BLANK_OR_NULL;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_TOPIC_NAME_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_DESCRIPTION;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NAME;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MIN_SIZE_NAME;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Topic {

    private Long id;

    @NotBlank(message = KEY_TOPIC_NAME_BLANK_OR_NULL)
    @Size(min = MIN_SIZE_NAME, max = MAX_SIZE_NAME, message = KEY_TOPIC_NAME_LENGTH_CONSTRAINT)
    private String name;

    @Size(max = MAX_SIZE_DESCRIPTION, message = KEY_TOPIC_DESCRIPTION_LENGTH_CONSTRAINT)
    private String description;

    private StatusEnum status;
    private Long skillId;
}
