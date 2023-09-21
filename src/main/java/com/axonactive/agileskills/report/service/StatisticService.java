package com.axonactive.agileskills.report.service;

import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.entity.RequireEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.service.PositionService;
import com.axonactive.agileskills.report.service.model.YearlyStatistics;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestScoped
public class StatisticService {
    @Inject
    private PositionService positionService;

    public List<YearlyStatistics> getStatisticByYear(Integer year) {

        List<YearlyStatistics> yearlyStatisticsList = new ArrayList<>();

        List<PositionEntity> positionEntityList = positionService.getByYear(year);

        double totalPosition = positionEntityList.size();

        Map<String, List<RequiredSkillEntity>> skillFrequency = getMapSkillFrequency(positionEntityList);

        for (Map.Entry<String, List<RequiredSkillEntity>> skill : skillFrequency.entrySet()) {

            yearlyStatisticsList.add(createYearlyStatistic(totalPosition, skill));
        }

        sortDescByAppearAndMustHave(yearlyStatisticsList);

        return yearlyStatisticsList;
    }

    private void sortDescByAppearAndMustHave(List<YearlyStatistics> yearlyStatisticsList) {
        yearlyStatisticsList.sort((a, b) -> {

            int compareByAppearance = Double.compare(b.getAppearancePercentage(), a.getAppearancePercentage());


            if (compareByAppearance == 0) {
                return Double.compare(b.getMustHavePercentage(), a.getMustHavePercentage());
            }

            return compareByAppearance;
        });
    }

    public Map<String, List<RequiredSkillEntity>> getMapSkillFrequency(List<PositionEntity> positionEntityList) {
        return positionEntityList.stream().flatMap(pos -> pos.getRequiredSkillList().stream().filter(Objects::nonNull))
                .collect(Collectors.groupingBy(rSkill -> rSkill.getSkill().getName()));
    }


    private YearlyStatistics createYearlyStatistic(double totalPosition, Map.Entry<String, List<RequiredSkillEntity>> skill) {
        String skillName = skill.getKey();
        double frequency = skill.getValue().size();

        double appearPercentage = Math.round(frequency * 100 / totalPosition);
        double countMustHave = skill.getValue().stream().filter(rs -> RequireEnum.MUST_HAVE.equals(rs.getRequire())).count();
        double mustHavePercentage = Math.round(countMustHave * 100 / totalPosition);
        double niceToHavePercentage = Math.round(appearPercentage - mustHavePercentage);

        return new YearlyStatistics(skillName, appearPercentage, mustHavePercentage, niceToHavePercentage);
    }
}
