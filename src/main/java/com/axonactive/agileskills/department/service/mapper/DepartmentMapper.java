package com.axonactive.agileskills.department.service.mapper;

import com.axonactive.agileskills.base.mapper.BaseMapper;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import com.axonactive.agileskills.department.service.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "jakarta",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper extends BaseMapper<DepartmentEntity, Department> {
}
