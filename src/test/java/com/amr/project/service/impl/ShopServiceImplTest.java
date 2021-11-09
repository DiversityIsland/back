package com.amr.project.service.impl;

import com.amr.project.converter.ShopMapper;
import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Shop;
import com.amr.project.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceImplTest {

    @Autowired
    ShopServiceImpl shopService;
    private ShopDto shopDto;
    private User user;

    @Mock
    private ShopMapper shopMapper;

    @Test
    public void newShop() {
        shopDto = new ShopDto();
        shopDto.setId(88L);
        shopDto.setLocation("a");
        shopDto.setEmail("a@mail.com");
        shopDto.setPhone("11111111111");
        shopDto.setName("1a");
        shopDto.setDescription("1");

        user = new User();
        user.setUsername("Anton");
        user.setPassword("1111");
        user.setEmail("as@mail.com");
        user.setPhone("99999999999");
        user.setBirthday(new Calendar.Builder().setDate(2001, 3, 15).build());
        Shop shop = shopService.addShop(shopDto, user);

        assertEquals("a@mail.com", shop.getEmail());
        assertEquals("11111111111", shop.getPhone());
        assertNotNull(shop.getLogo());
        assertNotNull(shop.getUser());
        assertNotNull(shop.getLocation());
    }

    @Test
    public void deleteShop() {
        shopService.delete(shopMapper.shopDtoToShop(shopDto));

        Shop deleteShop = shopService.getByKey(shopDto.getId());
        assertNull(deleteShop.getUser());
        assertNull(deleteShop.getLocation());
        assertNull(deleteShop.getName());
        assertNull(deleteShop.getPhone());
        assertNull(deleteShop.getDescription());
    }
}
