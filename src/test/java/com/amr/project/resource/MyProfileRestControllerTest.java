package com.amr.project.resource;

import com.amr.project.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql("data.sql"),
        @Sql(scripts = "dropTable.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class MyProfileRestControllerTest extends AbstractIntegrationTest{


        @Autowired
        private MockMvc mockMvc;

        @Test
        void getUser() throws Exception {
            this.mockMvc.perform(get("/api/user/1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void getUser_invalid_id() throws Exception {
            this.mockMvc.perform(get("/api/user/5"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }


        @Test
        void updateUser() throws Exception {
            this.mockMvc.perform(put("/api/user/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{" +
                            "\"id\": \"3\"," +
                            "\"email\": \"name3@mail.ru\"," +
                            "\"username\": \"name3@mail.ru\"," +
                            "\"password\": \"333333\"," +
                            "\"phone\": \"89269993333\"," +
                            "\"firstName\": \"name3\"," +
                            "\"lastName\": \"lastname3\"," +
                            "\"age\": \"30\"," +
                            "\"roles\": " +
                            "[{" +
                            "\"id\": \"3\"," +
                            "\"name\": \"USER\"," +
                            "\"authority\": \"USER\"" +
                            "}]," +
                            "\"address\": " +
                            "{" +
                            "\"id\": \"3\"," +
                            "\"cityIndex\": \"city_index3\"," +
                            "\"country\": \"country3\"," +
                            "\"city\": \"city4\"," +
                            "\"street\": \"street3\"," +
                            "\"house\": \"3\"" +
                            "}," +
                            "\"birthday\": \"22-11-1977\"," +
                            "\"gender\": \"MALE\"" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void updateUser_invalid_id() throws Exception {
            this.mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": \"3\"," +
                        "\"email\": \"name3@mail.ru\"," +
                        "\"username\": \"name3@mail.ru\"," +
                        "\"password\": \"333333\"," +
                        "\"phone\": \"89269993333\"," +
                        "\"firstName\": \"name3\"," +
                        "\"lastName\": \"lastname3\"," +
                        "\"age\": \"30\"," +
                        "\"roles\": " +
                        "[{" +
                        "\"id\": \"3\"," +
                        "\"name\": \"USER\"," +
                        "\"authority\": \"USER\"" +
                        "}]," +
                        "\"address\": " +
                        "{" +
                        "\"id\": \"3\"," +
                        "\"cityIndex\": \"city_index3\"," +
                        "\"country\": \"country3\"," +
                        "\"city\": \"city3\"," +
                        "\"street\": \"street3\"," +
                        "\"house\": \"3\"" +
                        "}," +
                        "\"birthday\": \"22-11-1977\"," +
                        "\"gender\": \"MALE\"" +
                        "}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

