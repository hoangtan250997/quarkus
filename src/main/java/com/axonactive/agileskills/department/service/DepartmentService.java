package com.axonactive.agileskills.department.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.dao.DepartmentDAO;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.service.mapper.DepartmentMapper;
import com.axonactive.agileskills.department.service.model.Department;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.List;

//@Stateless
@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentDAO departmentDAO;
    @Inject
    DepartmentMapper departmentMapper;

    public List<DepartmentEntity> getByStatus(StatusEnum status) {
        List<DepartmentEntity> departmentEntityList = departmentDAO.findByStatus(status);
        return (departmentEntityList);
    }
}

