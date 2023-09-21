package com.axonactive.agileskills.report.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearlyStatistics {
    private String skillName;
    private Double appearancePercentage;
    private Double mustHavePercentage;
    private Double niceToHavePercentage;

}
