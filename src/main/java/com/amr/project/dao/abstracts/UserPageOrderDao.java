package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.Order;

import java.util.List;

public interface UserPageOrderDao extends ReadWriteDAO<Order,Long> {
    List<Order> getOrdersByUserId(Long userId);
}
