package com.axonactive.agileskills.department.team.dao;

import com.axonactive.agileskills.base.dao.BaseDAO;
import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.department.team.entity.TeamEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TeamDAO extends BaseDAO<TeamEntity> {

    public TeamDAO() {
        super(TeamEntity.class);
    }

    public List<TeamEntity> findByDepartmentIdAndStatus(Long departmentId, StatusEnum status) {
        return em.createQuery("SELECT t FROM TeamEntity t " +
                        "WHERE t.department.id = :departmentId AND t.status = :status ORDER BY t.name", TeamEntity.class)
                .setParameter("departmentId", departmentId)
                .setParameter("status", status)
                .getResultList();
    }

    public List<TeamEntity> findByStatus(StatusEnum status) {
        return em.createQuery("SELECT t FROM TeamEntity t " +
                        "WHERE t.status = :status ORDER BY t.name", TeamEntity.class)
                .setParameter("status", status).getResultList();
    }

    public Optional<TeamEntity> findByIdAndStatus(Long id, StatusEnum status) {
        List<TeamEntity> teamList = em.createQuery("SELECT t FROM TeamEntity t " +
                        "WHERE t.id = :id AND t.status = :status", TeamEntity.class)
                .setParameter("id", id)
                .setParameter("status", status)
                .getResultList();

        return teamList.isEmpty() ? Optional.empty() : Optional.of(teamList.get(0));
    }
}