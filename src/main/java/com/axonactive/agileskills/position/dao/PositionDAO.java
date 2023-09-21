package com.axonactive.agileskills.position.dao;

import com.axonactive.agileskills.base.dao.BaseDAO;
import com.axonactive.agileskills.position.entity.PositionEntity;
import com.axonactive.agileskills.position.entity.PositionStatusEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PositionDAO extends BaseDAO<PositionEntity> {

    public PositionDAO() {
        super(PositionEntity.class);
    }

    public PositionDAO(EntityManager em) {
        super(PositionEntity.class);
        this.em = em;
    }

    public List<PositionEntity> findByStatus(PositionStatusEnum status) {
        return em.createQuery("SELECT p FROM PositionEntity p, TeamEntity t, DepartmentEntity d " +
                        "WHERE p.status = :status and p.team.id = t.id and t.department.id = d.id "
                        + " ORDER BY p.name, t.name, d.name", PositionEntity.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Integer> findYears() {
        return em.createQuery("SELECT EXTRACT(YEAR FROM p.openedDate) " +
                        "FROM PositionEntity p " +
                        "GROUP BY EXTRACT(YEAR FROM p.openedDate) " +
                        "ORDER BY EXTRACT(YEAR FROM p.openedDate) DESC", Integer.class)
                .getResultList();
    }

    public List<PositionEntity> findByYear(Integer year) {
        return em.createQuery("SELECT p FROM PositionEntity p WHERE EXTRACT(YEAR FROM p.openedDate) = :year " +
                        "OR EXTRACT(YEAR FROM p.closedDate) = :year", PositionEntity.class)
                .setParameter("year", year)
                .getResultList();
    }

    public Optional<PositionEntity> findByIdAndStatus(Long id, PositionStatusEnum status) {
        List<PositionEntity> positionList = em.createQuery("SELECT p FROM PositionEntity p " +
                        "WHERE p.id = :id and p.status = :status", PositionEntity.class)
                .setParameter("id", id)
                .setParameter("status", status)
                .getResultList();

        return positionList.isEmpty() ? Optional.empty() : Optional.of(positionList.get(0));
    }

    public PositionEntity close(Long id) {
        PositionEntity closedPosition = findByIdAndStatus(id, PositionStatusEnum.OPEN).orElse(null);
        if (closedPosition != null) {
            closedPosition.setStatus(PositionStatusEnum.CLOSE);
            closedPosition.setClosedDate(LocalDateTime.now());
            return closedPosition;
        }
        return null;
    }

    public List<PositionEntity> findByTeamIdAndStatus(Long teamId, PositionStatusEnum status) {
        return em.createQuery("SELECT p FROM PositionEntity p " +
                        "WHERE p.team.id = :teamId AND p.status = :status", PositionEntity.class)
                .setParameter("teamId", teamId)
                .setParameter("status", status)
                .getResultList();
    }

    public List<PositionEntity> searchStatus(String input, PositionStatusEnum statusEnum) {

        String word = "%" + input.trim().toLowerCase() + "%";

        return em.createQuery("SELECT DISTINCT p " +
                                "FROM PositionEntity p " +
                                "LEFT JOIN p.requiredSkillList rs " +
                                "LEFT JOIN rs.skill s " +
                                "LEFT JOIN rs.requiredTopicList rt " +
                                "LEFT JOIN rt.topic t " +
                                "WHERE p.team.id IN (" +
                                "    SELECT t.id" +
                                "    FROM TeamEntity t" +
                                "    INNER JOIN DepartmentEntity d ON t.department.id = d.id" +
                                "    ORDER BY t.name, d.name" +
                                ") AND " +
                                "(LOWER(p.name) LIKE :word OR LOWER(p.note) LIKE :word OR " +
                                "LOWER(s.name) LIKE :word OR LOWER(s.description) LIKE :word OR " +
                                "LOWER(t.name) LIKE :word OR LOWER(t.description) LIKE :word) " +
                                "AND p.status = :status " +
                                "ORDER BY p.name"
                        , PositionEntity.class)
                .setParameter("word", word)
                .setParameter("status", statusEnum)
                .getResultList();
    }
}
