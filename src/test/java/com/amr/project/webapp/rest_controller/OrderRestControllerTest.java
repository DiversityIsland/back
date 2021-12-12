package com.amr.project.webapp.rest_controller;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderRestControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    void setStatusStart() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/order/start/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void setStatusComplete() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/order/complete/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

    }

    @Test
    void setStatusSent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/order/sent/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void setStatusDelivered() throws  Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/order/sent/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}