package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.ItemMapper;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.service.abstracts.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MainPageRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainPageRestController mainPageRestController;

    private ItemDto getItemDtoById(Long id) {
        return itemMapper.itemToItemDto(itemService.findItemById(id));
    }

    @Test
    void getLastPageNumItemsByCategoryId() throws Exception {
        mockMvc.perform(get("/api/mainpage/item/lastpage/category/{id}/{pageSize}", 3, 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("2"));
    }

    @Test
    void getItemsByCategoryIdWithPagination() throws Exception {
        ItemDto itemDto = getItemDtoById(14L);
        mockMvc.perform(get("/api/mainpage/item/category/{id}/{pagenum}/{pageSize}", 5, 2, 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(itemDto))));
    }
}