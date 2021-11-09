package com.amr.project.webapp.rest_controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value="user2")
public class ShopRestControllerTest {
    @Autowired
    ShopRestController shopRestController;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getShop() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shop/3"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
