package com.axonactive.agileskills.report;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.requiredskill.entity.LevelEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequireEnum;
import com.axonactive.agileskills.position.requiredskill.entity.RequiredSkillEntity;
import com.axonactive.agileskills.position.service.PositionService;
import com.axonactive.agileskills.report.service.StatisticService;
import com.axonactive.agileskills.report.service.model.YearlyStatistics;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.axonactive.agileskills.position.entity.PositionStatusEnum.OPEN;
import static com.axonactive.agileskills.position.requiredskill.entity.LevelEnum.MASTER;
import static com.axonactive.agileskills.position.requiredskill.entity.RequireEnum.MUST_HAVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class StatisticServiceTest {
    @InjectMocks
    private StatisticService statisticService;

    @Mock
    private PositionService positionService;

    @Test
    void getStatisticByYear_InputYearIs2021_ReturnYearlyStatistics() {
        Integer year = 2021;

        List<PositionEntity> positionEntityList = new ArrayList<>();
        positionEntityList.add(getPosition());
        positionEntityList.add(getPosition1());

        when(positionService.getByYear(year)).thenReturn(positionEntityList);

        List<YearlyStatistics> yearlyStatisticsList = statisticService.getStatisticByYear(year);

        // Assert that the yearlyStatisticsList is not empty
        assertEquals(2, yearlyStatisticsList.size());

        assertEquals("Java", yearlyStatisticsList.get(0).getSkillName());
        assertEquals(100.0, yearlyStatisticsList.get(0).getAppearancePercentage());
        assertEquals(100.0, yearlyStatisticsList.get(0).getMustHavePercentage());
        assertEquals(0.0, yearlyStatisticsList.get(0).getNiceToHavePercentage());
        assertEquals("Python", yearlyStatisticsList.get(1).getSkillName());
        assertEquals(100.0, yearlyStatisticsList.get(1).getAppearancePercentage());
        assertEquals(100.0, yearlyStatisticsList.get(1).getMustHavePercentage());
        assertEquals(0.0, yearlyStatisticsList.get(1).getNiceToHavePercentage());
    }

    @Test
    void sortDescByAppearAndMustHave_positiveCase_ReturnDescList() {
        Integer year = 2021;

        List<PositionEntity> positionEntityList = new ArrayList<>();
        positionEntityList.add(getPosition());
        positionEntityList.add(getPosition1());
        positionEntityList.add(getPosition2());
        positionEntityList.add(getPosition3());

        when(positionService.getByYear(year)).thenReturn(positionEntityList);

        List<YearlyStatistics> yearlyStatisticsList = statisticService.getStatisticByYear(year);

        assertEquals("Python", yearlyStatisticsList.get(1).getSkillName());
        assertEquals("Java", yearlyStatisticsList.get(0).getSkillName());
        assertEquals("C#", yearlyStatisticsList.get(2).getSkillName());
    }


    @Test
    void getMapSkillFrequency_PositiveCase() {

        List<PositionEntity> positionEntityList = new ArrayList<>();
        positionEntityList.add(getPosition());
        positionEntityList.add(getPosition1());

        // Call the method
        Map<String, List<RequiredSkillEntity>> result = statisticService.getMapSkillFrequency(positionEntityList);

        // Assert the result
        assertEquals(2, result.size());
    }

    @Test
    void getMapSkillFrequency_NullSkills_ReturnNull() {
        // Create sample PositionEntity list with null skills
        PositionEntity position1 = new PositionEntity();
        position1.setRequiredSkillList(Arrays.asList(null, null));

        PositionEntity position2 = new PositionEntity();
        position2.setRequiredSkillList(new ArrayList<>());

        List<PositionEntity> positionEntityList = Arrays.asList(position1, position2);

        // Call the method
        Map<String, List<RequiredSkillEntity>> result = statisticService.getMapSkillFrequency(positionEntityList);

        // Assert the result
        assertEquals(0, result.size());
    }

    private PositionEntity getPosition1() {
        LocalDateTime createdDate = LocalDateTime.of(2021, 1, 1, 8, 0, 0);
        return PositionEntity.builder()
                .id(1L)
                .createdDate(createdDate)
                .name("Sample Position 23")
                .quantity(2)
                .status(OPEN)
                .note("a sample position.")
                .requiredSkillList(getRequiredSkillList())
                .build();
    }

    private PositionEntity getPosition2() {
        LocalDateTime createdDate = LocalDateTime.of(2021, 1, 1, 8, 0, 0);
        return PositionEntity.builder()
                .id(1L)
                .createdDate(createdDate)
                .name("Sample Position 23")
                .quantity(2)
                .status(OPEN)
                .note("a sample position.")
                .requiredSkillList(getRequiredSkillList())
                .build();
    }

    private PositionEntity getPosition3() {
        LocalDateTime createdDate = LocalDateTime.of(2021, 1, 1, 8, 0, 0);
        return PositionEntity.builder()
                .id(1L)
                .createdDate(createdDate)
                .name("Sample Position 23")
                .quantity(2)
                .status(OPEN)
                .note("a sample position.")
                .requiredSkillList(getRequiredSkillList1())
                .build();
    }

    private PositionEntity getPosition() {
        LocalDateTime createdDate = LocalDateTime.of(2021, 1, 1, 8, 5, 0);
        return PositionEntity.builder()
                .id(1L)
                .createdDate(createdDate)
                .name("Sample Position")
                .quantity(10)
                .status(OPEN)
                .note("a sample position.")
                .requiredSkillList(getRequiredSkillList())
                .build();
    }

    private List<RequiredSkillEntity> getRequiredSkillList() {
        return Arrays.asList(getInputRequiredSkillJava(), getInputRequireSkillPyThon());
    }

    private List<RequiredSkillEntity> getRequiredSkillList1() {
        return Arrays.asList(getInputRequiredSkillJava(), getInputRequireSkillC(), getInputRequireSkillPyThon());
    }


    private SkillEntity getJava() {
        return SkillEntity.builder().status(StatusEnum.ACTIVE).name("Java").build();
    }

    private SkillEntity getC() {
        return SkillEntity.builder().status(StatusEnum.ACTIVE).name("C#").build();
    }

    private SkillEntity getPython() {
        return SkillEntity.builder().status(StatusEnum.ACTIVE).name("Python").build();
    }

    private RequiredSkillEntity getInputRequireSkillPyThon() {
        return RequiredSkillEntity.builder()
                .skill(getPython())
                .require(RequireEnum.MUST_HAVE)
                .level(LevelEnum.MASTER)
                .note("Input DTO Required Skill 1")
                .requiredTopicList(null)
                .build();
    }

    private RequiredSkillEntity getInputRequireSkillC() {
        return RequiredSkillEntity.builder()
                .skill(getC())
                .require(RequireEnum.MUST_HAVE)
                .level(LevelEnum.MASTER)
                .note("Input DTO Required Skill 1")
                .build();
    }

    private RequiredSkillEntity getInputRequiredSkillJava() {
        return RequiredSkillEntity.builder()
                .id(1L)
                .skill(getJava())
                .require(MUST_HAVE)
                .level(MASTER)
                .note("Input DTO Required Skill 1")
                .build();
    }

}