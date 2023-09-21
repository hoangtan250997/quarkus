package com.axonactive.agileskills.base.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Contract for a generic DAO.
 *
 * @param <E> - Entity type parameter.
 */

@RequiredArgsConstructor
@ApplicationScoped
public abstract class BaseDAO<E> {

    private final Class<E> entityClass;

    @Inject
    protected EntityManager em;

    public E create(E entity) {
        em.persist(entity);
        return entity;
    }

    public E update(E entity) {
        E updatedEntity = em.merge(entity);
        em.flush();
        return updatedEntity;
    }

    public Optional<E> findById(Long id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<E> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);
        cq.select(root);

        return em.createQuery(cq).getResultList();
    }
}