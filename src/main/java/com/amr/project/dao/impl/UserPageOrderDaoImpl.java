package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.UserPageOrderDao;
import com.amr.project.model.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserPageOrderDaoImpl extends ReadWriteDAOImpl<Order, Long> implements UserPageOrderDao {

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return entityManager.createQuery("select o from Order o where o.user.id = :userId")
                .setParameter("userId", userId)
                .getResultList();
    }
}
