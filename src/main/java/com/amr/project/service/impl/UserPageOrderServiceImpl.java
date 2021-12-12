package com.amr.project.service.impl;

import com.amr.project.converter.OrderMapper;
import com.amr.project.dao.abstracts.UserPageOrderDao;
import com.amr.project.model.dto.OrderDto;
import com.amr.project.model.entity.Order;
import com.amr.project.service.abstracts.UserPageOrderService;
import com.amr.project.webapp.handler.PaymentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserPageOrderServiceImpl extends ReadWriteServiceImpl<Order, Long> implements UserPageOrderService {

    private UserPageOrderDao userPageOrderDao;
    private OrderMapper orderMapper;
    private final PaymentApi paymentApi;

    @Autowired
    public UserPageOrderServiceImpl(UserPageOrderDao userPageOrderDao,
                                    OrderMapper orderMapper, PaymentApi paymentApi) {
        super(userPageOrderDao);
        this.userPageOrderDao = userPageOrderDao;
        this.orderMapper = orderMapper;
        this.paymentApi = paymentApi;
    }


    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {

        return orderMapper.listOrderToDto(userPageOrderDao.getOrdersByUserId(userId));
    }

    @Override
    public void checkQiwiPaymentStatus(Long userId) {
        List<OrderDto> ordersFromBase = orderMapper.listOrderToDto(userPageOrderDao.getOrdersByUserId(userId));
        for (OrderDto order : ordersFromBase) {

            String id = order.getId().toString();
            paymentApi.getStatus(id);


        }
    }
}
