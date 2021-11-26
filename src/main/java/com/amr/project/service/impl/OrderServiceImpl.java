package com.amr.project.service.impl;

import com.amr.project.converter.ItemMapper;
import com.amr.project.dao.abstracts.OrderDao;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.dto.OrderDto;
import com.amr.project.model.entity.*;
import com.amr.project.model.enums.PaymentStatus;
import com.amr.project.model.enums.Status;
import com.amr.project.service.abstracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl extends ReadWriteServiceImpl<Order, Long> implements OrderService {
    private OrderDao orderDao;
    private ItemMapper itemMapper;
    private CountryService countryService;
    private CityService cityService;
    private AddressService addressService;
    private ItemService itemService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao,
                            ItemMapper itemMapper,
                            CountryService countryService,
                            CityService cityService,
                            AddressService addressService,
                            ItemService itemService) {
        super(orderDao);
        this.orderDao = orderDao;
        this.itemMapper = itemMapper;
        this.countryService = countryService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.itemService = itemService;
    }
    //Методы используются на странице order page
    @Override
    public Order collectOrderByUserAndItems(List<ItemDto> items, User user) {
        Order order = new Order();

        order.setItems(itemMapper.toItems(items));
        order.setAddress(user.getAddress());
        order.setUser(user);
        order.setBuyerName(user.getFirstName());
        order.setBuyerPhone(user.getPhone());
        order.setDate(Calendar.getInstance());
        order.setPaymentStatus(PaymentStatus.WAITING);
        order.setStatus(Status.START);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice())
                .reduce((s1, s2) -> s1.add(s2))
                .get();

        order.setTotal(total);
        super.persist(order);
        return order;
    }

    @Override
    public void updateAddressAndUserInfo(Long id, OrderDto orderDto) {
        Order order = super.getByKey(id);
        if(order != null) {
            order.setBuyerName(orderDto.getBuyerName());
            order.setBuyerPhone(orderDto.getBuyerPhone());
            Address address = order.getAddress();
            if(address != null) {
                Country country = address.getCountry();
                City city = address.getCity();
                country.setName(orderDto.getCountry());
                city.setName(orderDto.getCity());
                address.setStreet(orderDto.getStreet());
                address.setHouse(orderDto.getHouse());
                countryService.update(country);
                cityService.update(city);
                addressService.update(address);
            }
            super.update(order);
        }
    }

    @Override
    public void deleteItemInOrder(Long orderId, Long itemId) {
        Order order = super.getByKey(orderId);
        Item item = itemService.getByKey(itemId);
        order.setTotal(order.getTotal().subtract(item.getPrice()));
        super.update(order);
        orderDao.deleteItemInOrder(orderId, itemId);
    }
    //=========================================================

}
