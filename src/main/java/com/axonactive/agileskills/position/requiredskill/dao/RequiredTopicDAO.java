package com.axonactive.agileskills.position.requiredskill.dao;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

@ApplicationScoped
public class RequiredTopicDAO {

    @Inject
    private EntityManager em;

    public void deleteInList(List<Long> idList) {
        Query query = em.createQuery("DELETE FROM RequiredTopicEntity rt WHERE rt.id IN :idList")
                .setParameter("idList", idList);
        query.executeUpdate();
    }
}

