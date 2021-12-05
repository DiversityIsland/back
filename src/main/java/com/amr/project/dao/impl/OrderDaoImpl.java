package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.OrderDao;
import com.amr.project.model.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl extends ReadWriteDAOImpl<Order, Long> implements OrderDao {
    @Override
    public void deleteItemInOrder(Long orderId, Long itemId) {
        entityManager.createNativeQuery("delete from orders_item where orders_id = :orderId and item_id = :itemId")
                .setParameter("orderId", orderId)
                .setParameter("itemId", itemId)
                .executeUpdate();
    }


}
