package com.axonactive.agileskills.user.dao;

import com.axonactive.agileskills.base.dao.BaseDAO;
import com.axonactive.agileskills.user.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDAO extends BaseDAO<UserEntity> {

    public UserDAO() {
        super(UserEntity.class);
    }

    public Optional<UserEntity> findByEmail(String email) {
        List<UserEntity> userEntityList = em.createQuery("SELECT u FROM UserEntity u " +
                        "WHERE LOWER(trim(both from u.email)) LIKE LOWER(trim(both from :email))", UserEntity.class)
                .setParameter("email", email)
                .getResultList();

        return userEntityList.isEmpty() ? Optional.empty() : Optional.of(userEntityList.get(0));
    }
}
