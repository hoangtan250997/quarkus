package com.axonactive.agileskills.department.team.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.dao.DepartmentDAO;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.team.dao.TeamDAO;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.department.team.service.mapper.TeamMapper;
import com.axonactive.agileskills.department.team.service.model.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamDAO teamDAO;

    @Mock
    private DepartmentDAO departmentDAO;

    @Mock
    private TeamMapper teamMapper;

    @Test
    void getByDepartmentIdAndStatus_ExistedDepartmentId_ReturnDTOList() throws ResourceNotFoundException {
        DepartmentEntity department = activeDepartment();
        List<TeamEntity> activeTeamList = activeTeamList();
        List<Team> activeTeamDTOList = activeTeamDTOList();

        when(departmentDAO.findByIdAndStatus(1L, StatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(department));
        when(teamDAO.findByDepartmentIdAndStatus(department.getId(), StatusEnum.ACTIVE)).thenReturn(activeTeamList);
        when(teamMapper.toDTOList(activeTeamList)).thenReturn(activeTeamDTOList);

        assertEquals(activeTeamDTOList.size(),
                teamService.getByDepartmentIdAndStatus(1L, StatusEnum.ACTIVE).size());
    }

    @Test
    void getTeamList_DepartmentIdNotExisted_ThrowException() {
        when(departmentDAO.findByIdAndStatus(999L, StatusEnum.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class
                , () -> teamService.getByDepartmentIdAndStatus(999L, StatusEnum.ACTIVE));
    }

    @Test
    void getTeamList_InactiveDepartment_ThrowException() {
        when(departmentDAO.findByIdAndStatus(4L, StatusEnum.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class
                , () -> teamService.getByDepartmentIdAndStatus(4L, StatusEnum.ACTIVE));
    }


    @Test
    void getTeamList_StatusActive_ReturnDTOList() {
        List<TeamEntity> activeTeamList = activeTeamList();
        List<Team> activeTeamDTOList = activeTeamDTOList();

        when(teamDAO.findByStatus(StatusEnum.ACTIVE)).thenReturn(activeTeamList);
        when(teamMapper.toDTOList(activeTeamList)).thenReturn(activeTeamDTOList);

        assertEquals(activeTeamDTOList.size(), teamService.getByStatus(StatusEnum.ACTIVE).size());
    }

    private DepartmentEntity activeDepartment() {
        return DepartmentEntity.builder()
                .name("Schwyz")
                .status(StatusEnum.ACTIVE)
                .build();
    }

    private DepartmentEntity inactiveDepartment() {
        return DepartmentEntity.builder()
                .name("INACTIVE1689682076679_Wallis")
                .status(StatusEnum.INACTIVE)
                .build();
    }

    private List<TeamEntity> activeTeamList() {
        TeamEntity team1 = TeamEntity.builder()
                .name("Wow")
                .status(StatusEnum.ACTIVE)
                .build();

        TeamEntity team2 = TeamEntity.builder()
                .name("Next")
                .status(StatusEnum.ACTIVE)
                .build();

        return Arrays.asList(team1, team2);
    }

    private List<Team> activeTeamDTOList() {
        Team teamDTO1 = Team.builder()
                .name("Wow")
                .status(StatusEnum.ACTIVE)
                .build();

        Team teamDTO2 = Team.builder()
                .name("Next")
                .status(StatusEnum.ACTIVE)
                .build();

        return Arrays.asList(teamDTO1, teamDTO2);
    }
}