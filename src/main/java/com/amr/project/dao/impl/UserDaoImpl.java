package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.UserDao;
import com.amr.project.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public List<User> findUsersByChatId(Long id) {
        return entityManager.createQuery("SELECT m FROM Chat c LEFT JOIN c.members m WHERE c.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Long findCountTotalElementBySearchName(String name) {
        return (Long) entityManager.createQuery("SELECT count(u.id) FROM User u WHERE CONCAT(LOWER( u.firstName),' ', LOWER(u.lastName)) LIKE :name")
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getSingleResult();
    }

    @Override
    public List<User> findUserBySearchNameWithPagination(String name, int pageNum, int pageSize) {
        return entityManager.createQuery("SELECT u FROM User u WHERE CONCAT(LOWER( u.firstName),' ', LOWER(u.lastName)) LIKE :name ORDER BY u.id", User.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<User> findConnectUsersById(Long currentId) {
        String query = "SELECT * FROM user u JOIN chat_members cm1 " +
                "ON (cm1.members_id = u.id AND cm1.chat_id IN (SELECT c.id FROM chat c " +
                "JOIN chat_members cm2 ON (c.id = cm2.chat_id) WHERE cm2.members_id = :id)) " +
                "WHERE cm1.members_id != :id";

        return entityManager.createNativeQuery(query, User.class)
                .setParameter("id", currentId)
                .getResultList();
    }
}

