package com.amr.project.service.impl;

import com.amr.project.converter.ItemMapper;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.dto.OrderDto;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Order;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    List<ItemDto> items = new ArrayList<>();

    OrderDto orderDto = new OrderDto();

    @Before
    public void createUserAndItem() {

        user.setUsername("Arseniy");
        user.setPassword("8800-");
        user.setEmail("as@mail.com");
        user.setPhone("77777777777");
        user.setBirthday(new Calendar.Builder().setDate(2001, 3, 15).build());
        userService.persist(user);

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

    }


    @Test
    public void collectOrder() {

        Order order = orderService.collectOrderByUserAndItems(items, user);


        assertEquals("as@mail.com", order.getUser().getEmail());
        assertEquals("77777777777", order.getBuyerPhone());
        assertNotNull(order.getStatus());
        assertNotNull(order.getTotal());
        assertNotNull(order.getId());
    }


    @Test
    public void updateAddressAndUInfo() {
        Order order = orderService.collectOrderByUserAndItems(items, user);

        orderDto.setBuyerPhone("10987654321");
        orderDto.setBuyerName("Petya");


        orderService.updateAddressAndUserInfo(1L, orderDto);

        assertEquals("10987654321", orderService.getByKey(1L).getBuyerPhone());
        assertEquals("Petya", orderService.getByKey(1L).getBuyerName());

    }

    @Test
    public void DeleteItem() {
        Order order = orderService.collectOrderByUserAndItems(items, user);
        orderService.deleteItemInOrder(1L, 1L);

        assertFalse(order.getItems().isEmpty());

    }
}
