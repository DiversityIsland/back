package com.amr.project.service.impl;

import com.amr.project.converter.ItemMapper;
import com.amr.project.converter.OrderMapper;

import com.amr.project.model.dto.ItemDto;

import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Order;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    ItemService itemService;

    User user = new User();

//    @Mock
//    private OrderMapper orderMapper;

    @Before
    public void createUser(){

        user.setUsername("Arseniy");
        user.setPassword("8800-");
        user.setEmail("as@mail.com");
        user.setPhone("99999999999");
        user.setBirthday(new Calendar.Builder().setDate(2001, 3, 15).build());
        userService.persist(user);

    }


    @Test
    public void collectOrder(){
        List<ItemDto> items = new ArrayList<>();
        Item item = Item.builder()
                .name("Маска медицинская")
                .basePrice(BigDecimal.valueOf(10))
                .price(BigDecimal.valueOf(30))
                .count(100)
                .rating(1)
                .description("Маска медицинская")
                .isModerated(false)
                .isModerateAccept(false)
                .build();

        itemService.persist(item);
        items.add(itemMapper.itemToItemDto(item));

        Order order = orderService.collectOrderByUserAndItems(items,user);

        assertEquals("as@mail.com", user.getEmail());
        assertEquals("99999999999", user.getPhone());
        assertNotNull(order.getStatus());
        assertNotNull(order.getTotal());
        assertNotNull(order.getId());



    }

    @Test
    public void DeleteItem(){
        orderService.deleteItemInOrder(1L,1L);

    }
}
