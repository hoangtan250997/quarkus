package com.axonactive.agileskills.position.service.model;

import com.axonactive.agileskills.base.utility.CustomOffsetDateTimeDeserializer;
import com.axonactive.agileskills.base.utility.CustomOffsetDateTimeSerializer;
import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import com.axonactive.agileskills.position.requiredskill.service.model.RequiredSkill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_NAME_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_NAME_NULL_OR_BLANK;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_NOTE_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_QUANTITY_GREATER_THAN_0;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_QUANTITY_LESS_THAN_100;
import static com.axonactive.agileskills.base.exception.ErrorMessage.KEY_POSITION_QUANTITY_NULL_OR_BLANK;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_QUANTITY;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NAME;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NOTE;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MIN_QUANTITY;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MIN_SIZE_NAME;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position {

    private static final String OFFSET_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private Long id;

    @NotBlank(message = KEY_POSITION_NAME_NULL_OR_BLANK)
    @Size(min = MIN_SIZE_NAME, max = MAX_SIZE_NAME, message = KEY_POSITION_NAME_LENGTH_CONSTRAINT)
    private String name;

    @Size(max = MAX_SIZE_NOTE, message = KEY_POSITION_NOTE_LENGTH_CONSTRAINT)
    private String note;

    @Enumerated(EnumType.STRING)
    private PositionStatusEnum status;

    @NotNull(message = KEY_POSITION_QUANTITY_NULL_OR_BLANK)
    @Min(value = MIN_QUANTITY, message = KEY_POSITION_QUANTITY_GREATER_THAN_0)
    @Max(value = MAX_QUANTITY, message = KEY_POSITION_QUANTITY_LESS_THAN_100)
    private Integer quantity;

    private Long teamId;
    private String teamName;
    private String departmentName;
    private List<RequiredSkill> requiredSkillList;

    @JsonSerialize(using = CustomOffsetDateTimeSerializer.class)
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OFFSET_DATE_TIME_PATTERN)
    private OffsetDateTime openedDate;

    @JsonSerialize(using = CustomOffsetDateTimeSerializer.class)
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OFFSET_DATE_TIME_PATTERN)
    private OffsetDateTime createdDate;

    @JsonSerialize(using = CustomOffsetDateTimeSerializer.class)
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OFFSET_DATE_TIME_PATTERN)
    private OffsetDateTime closedDate;
}
