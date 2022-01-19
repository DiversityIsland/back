package com.amr.project.service.impl;


import com.amr.project.model.dto.ItemDto;
import com.amr.project.service.abstracts.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemServiceImplTest {
    ItemService itemService;

    @Autowired
    public ItemServiceImplTest(ItemService itemService) {
        this.itemService = itemService;
    }

    @Test
    public void searche() {
        List<ItemDto> items = itemService.findByName("Телефон Samsung");
        for(ItemDto item : items) {
            System.out.println(item);
        }
    }
}
