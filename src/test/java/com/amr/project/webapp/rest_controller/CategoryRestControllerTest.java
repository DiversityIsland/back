package com.amr.project.webapp.rest_controller;


import com.amr.project.AbstractIntegrationTest;
import com.amr.project.model.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@WithUserDetails(value = "user2")
@AutoConfigureMockMvc
public class CategoryRestControllerTest extends AbstractIntegrationTest {
    @Autowired
    CategoryRestController categoryRestController;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void addCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(88L);
        categoryDto.setName("packet");

        ResponseEntity<Void> responseEntity = categoryRestController.addCategory(categoryDto);
        assertEquals(HttpStatus.CREATED, responseEntity);
    }

    @Test
    public void invalidateAddCategory() {
        CategoryDto categoryDto = new CategoryDto();

        ResponseEntity<Void> responseEntity = categoryRestController.addCategory(categoryDto);
        assertEquals(HttpStatus.CREATED, responseEntity);
    }

    @Test
    public void deleteCategory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/1")    )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void invalidateDeleteCategory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/100")    )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
