package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends ReadWriteDAO<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findUsersByChatId(Long id);

    Long findCountTotalElementBySearchName(String name);

    List<User> findUserBySearchNameWithPagination(String name, int pageNum, int pageSize);

    List<User> findConnectUsersById(Long currentId);
}
