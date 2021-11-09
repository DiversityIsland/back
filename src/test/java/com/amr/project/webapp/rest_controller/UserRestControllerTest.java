package com.amr.project.webapp.rest_controller;

import com.amr.project.AbstractIntegrationTest;
import com.amr.project.model.dto.UserDto;
import com.amr.project.model.entity.Role;
import com.amr.project.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Calendar;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/com.arm.project.webapp.rest_controller/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithUserDetails(value = "petr")
class UserRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private UserRestController userRestController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("qqqq@mail.ru");
        userDto.setPassword("123456");
        userDto.setUsername("qqqqqqq");
        userDto.setPhone("89110249823");
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(3L, "USER"));
        userDto.setRoles(roles);
        userDto.setBirthday(new Calendar.Builder().setDate(2020, 5, 20).build());
        ResponseEntity<User> responseEntity = userRestController.addUser(userDto);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void addUser_invalid_data() {
        UserDto userDto = new UserDto();

        ResponseEntity<User> responseEntity = userRestController.addUser(userDto);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/3")    )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_invalid_id() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/4"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}