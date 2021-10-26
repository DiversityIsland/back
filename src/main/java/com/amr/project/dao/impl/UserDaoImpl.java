package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.UserDao;
import com.amr.project.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDAOImpl<User, Long> implements UserDao {
    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery("SELECT u from User u where u.username = :username", User.class)
                .setParameter("username", username).getResultList()
                .stream()
                .findAny();

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery("SELECT u from User u where u.email = :email", User.class)
                .setParameter("email", email).getResultList()
                .stream()
                .findAny();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return entityManager.createQuery("SELECT u from User u where u.id = :id", User.class)
                .setParameter("id", id).getResultList().stream().findAny();
    }


}

