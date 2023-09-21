package com.axonactive.agileskills.skill.dao;

import com.axonactive.agileskills.base.dao.BaseDAO;
import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SkillDAO extends BaseDAO<SkillEntity> {

    public SkillDAO() {
        super(SkillEntity.class);
    }

    public SkillEntity softDelete(Long id) {
        SkillEntity skill = findByIdAndStatus(id, StatusEnum.ACTIVE).orElse(null);
        if (skill != null && skill.getStatus().equals(StatusEnum.ACTIVE)) {
            skill.setStatus(StatusEnum.INACTIVE);
            skill.setName("INACTIVE" + System.currentTimeMillis() + "_" + skill.getName().trim());
            return skill;
        }
        return null;
    }

    public Optional<SkillEntity> findByName(String name) {
        List<SkillEntity> skillEntityList = em.createQuery("SELECT DISTINCT s FROM SkillEntity s " +
                        "WHERE LOWER(trim(both from s.name)) LIKE LOWER(trim(both from :name))", SkillEntity.class)
                .setParameter("name", name)
                .getResultList();

        return skillEntityList.isEmpty() ? Optional.empty() : Optional.of(skillEntityList.get(0));
    }

    public List<SkillEntity> findByStatus(StatusEnum status) {
        return em.createQuery("SELECT s FROM SkillEntity s " +
                        "WHERE s.status = :status ORDER BY s.name", SkillEntity.class)
                .setParameter("status", status).getResultList();
    }

    public Optional<SkillEntity> findByIdAndStatus(Long id, StatusEnum status) {
        List<SkillEntity> skillList = em.createQuery("SELECT s FROM SkillEntity s " +
                        "WHERE s.id = :id AND s.status = :status", SkillEntity.class)
                .setParameter("id", id)
                .setParameter("status", status)
                .getResultList();

        return skillList.isEmpty() ? Optional.empty() : Optional.of(skillList.get(0));
    }
}