package com.axonactive.agileskills.department.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.dao.DepartmentDAO;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.service.mapper.DepartmentMapper;
import com.axonactive.agileskills.department.service.model.Department;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentDAO departmentDAO;

    @Mock
    private DepartmentMapper departmentMapper;

    @Test
    void getDepartmentList_StatusActive_ReturnModelList() {
        List<DepartmentEntity> departmentList = activeDepartment();
        List<Department> departmentDTOList = activeDepartmentDTOList();

        when(departmentDAO.findByStatus(StatusEnum.ACTIVE)).thenReturn(departmentList);

        assertEquals(departmentDTOList.size(), departmentService.getByStatus(StatusEnum.ACTIVE).size());
    }

    private List<DepartmentEntity> activeDepartment() {
        DepartmentEntity department1 = DepartmentEntity.builder()
                .name("Zurich")
                .status(StatusEnum.ACTIVE)
                .build();

        DepartmentEntity department2 = DepartmentEntity.builder()
                .name("Geneve")
                .status(StatusEnum.ACTIVE)
                .build();

        return Arrays.asList(department1, department2);
    }

    private List<Department> activeDepartmentDTOList() {
        Department departmentDTO1 = Department.builder()
                .name("Zurich")
                .status(StatusEnum.ACTIVE)
                .build();

        Department departmentDTO2 = Department.builder()
                .name("Geneve")
                .status(StatusEnum.ACTIVE)
                .build();

        return Arrays.asList(departmentDTO1, departmentDTO2);
    }

}

