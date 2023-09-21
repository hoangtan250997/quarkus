package com.axonactive.agileskills.department.team.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.department.dao.DepartmentDAO;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.team.dao.TeamDAO;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import com.axonactive.agileskills.department.team.service.mapper.TeamMapper;
import com.axonactive.agileskills.department.team.service.model.Team;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TeamService {

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private TeamMapper teamMapper;

    public List<Team> getByDepartmentIdAndStatus(Long departmentId, StatusEnum status) throws ResourceNotFoundException {
        DepartmentEntity department = departmentDAO.findByIdAndStatus(departmentId, StatusEnum.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.KEY_DEPARTMENT_NOT_FOUND,
                        ErrorMessage.DEPARTMENT_NOT_FOUND));
        return teamMapper.toDTOList(teamDAO.findByDepartmentIdAndStatus(department.getId(), status));
    }

    public List<Team> getByStatus(StatusEnum status) {
        List<TeamEntity> teamEntityList = teamDAO.findByStatus(status);
        return teamMapper.toDTOList(teamEntityList);
    }
}
