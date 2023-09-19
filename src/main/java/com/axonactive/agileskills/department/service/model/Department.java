package com.axonactive.agileskills.department.service.model;

import com.axonactive.agileskills.base.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.axonactive.agileskills.base.exception.ErrorMessage.DEPARTMENT_NAME_LENGTH_CONSTRAINT;
import static com.axonactive.agileskills.base.exception.ErrorMessage.DEPARTMENT_NAME_NULL_OR_BLANK;
import static com.axonactive.agileskills.base.exception.ErrorMessage.MAX_SIZE_NAME;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {


    private Long id;

    @NotBlank(message = DEPARTMENT_NAME_NULL_OR_BLANK)
    @Size(max = MAX_SIZE_NAME, message = DEPARTMENT_NAME_LENGTH_CONSTRAINT)
    private String name;

    private StatusEnum status;
}
