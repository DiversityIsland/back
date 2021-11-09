package com.amr.project.service.impl;

import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class EmailServiceImplTest {

    @Autowired
    private EmailService emailService;

    @Test
    void addUser() {
        User user = new User();
        user.setPassword("123456");
        user.setEmail("anonymous@mail.ru");
        user.setUsername("Petr");

        boolean isUserCreated = emailService.addUser(user);

        assertTrue(isUserCreated);
        assertTrue(user.isActivate());
        assertNotNull(user.getActivationCode());
    }
}
