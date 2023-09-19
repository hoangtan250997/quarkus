package com.axonactive.agileskills.department.team.service.model;

import com.axonactive.agileskills.base.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.axonactive.agileskills.base.exception.ErrorMessage.TEAM_NAME_NULL_OR_BLANK;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    private Long id;

    @NotBlank(message = TEAM_NAME_NULL_OR_BLANK)
    private String name;

    private StatusEnum status;
    private Long departmentId;
    private String departmentName;
}
