package com.axonactive.agileskills.department.dao;

import com.axonactive.agileskills.base.dao.BaseDAO;
import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.entity.DepartmentEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentDAO extends BaseDAO<DepartmentEntity> {

    public DepartmentDAO() {
        super(DepartmentEntity.class);
    }

    public List<DepartmentEntity> findByStatus(StatusEnum status) {
        return em.createQuery("SELECT d FROM DepartmentEntity d " +
                        "WHERE d.status = :status ORDER BY d.name", DepartmentEntity.class)
                .setParameter("status", status).getResultList();
    }

    public Optional<DepartmentEntity> findByIdAndStatus(Long id, StatusEnum status) {
        List<DepartmentEntity> departmentList = em.createQuery("SELECT d FROM DepartmentEntity d " +
                        "WHERE d.id = :id AND d.status = :status ORDER BY d.name", DepartmentEntity.class)
                .setParameter("id", id)
                .setParameter("status", status)
                .getResultList();

        return departmentList.isEmpty() ? Optional.empty() : Optional.of(departmentList.get(0));
    }
}
