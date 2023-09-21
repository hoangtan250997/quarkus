package com.axonactive.agileskills.skill.topic.dao;

import com.axonactive.agileskills.base.dao.BaseDAO;
import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TopicDAO extends BaseDAO<TopicEntity> {

    public TopicDAO() {
        super(TopicEntity.class);
    }

    public TopicEntity softDelete(Long id) {
        TopicEntity topic = findByIdAndStatus(id, StatusEnum.ACTIVE).orElse(null);
        if (topic != null) {
            topic.setStatus(StatusEnum.INACTIVE);
            topic.setName("INACTIVE" + System.currentTimeMillis() + "_" + topic.getName().trim());
            return topic;
        }
        return null;
    }

    public List<TopicEntity> findBySkillId(Long skillId) {
        return em.createQuery("SELECT t FROM TopicEntity t WHERE t.skill.id = :skillId ORDER BY t.name", TopicEntity.class)
                .setParameter("skillId", skillId)
                .getResultList();
    }

    public List<TopicEntity> findBySkillIdAndStatus(Long skillId, StatusEnum status) {
        return em.createQuery("SELECT t FROM TopicEntity t " +
                        "WHERE t.skill.id = :skillId AND t.status = :status ORDER BY t.name", TopicEntity.class)
                .setParameter("skillId", skillId)
                .setParameter("status", status)
                .getResultList();
    }

    public Optional<TopicEntity> findByIdAndStatus(Long id, StatusEnum status) {
        List<TopicEntity> topicList = em.createQuery("SELECT t FROM TopicEntity t " +
                        "WHERE t.id = :id AND t.status = :status", TopicEntity.class)
                .setParameter("id", id)
                .setParameter("status", status)
                .getResultList();

        return topicList.isEmpty() ? Optional.empty() : Optional.of(topicList.get(0));
    }
}
