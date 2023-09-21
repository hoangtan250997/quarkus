package com.axonactive.agileskills.department.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.dao.DepartmentDAO;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.service.mapper.DepartmentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentDAO departmentDAO;
    @Inject
    DepartmentMapper departmentMapper;

    public List<DepartmentEntity> getByStatus(StatusEnum status) {
        return (departmentDAO.findByStatus(status));
    }
}

