package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.User;

import java.util.Optional;

public interface UserDao extends ReadWriteDAO<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findUserById(Long id);
}
