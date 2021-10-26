package com.amr.project.service.abstracts;

import com.amr.project.model.entity.Message;
import com.amr.project.model.entity.User;
import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MessageServiceTest {

    private final UserService userService;
    private final MessageService messageService;
    private final ItemService itemService;

    @Autowired
    public MessageServiceTest(UserService userService, MessageService messageService,
                              ItemService itemService) {
        this.userService = userService;
        this.messageService = messageService;
        this.itemService = itemService;
    }

    @Test
    void addMessage() {
        Message message = new Message();
        message.setTextMessage("Test message");
        messageService.addMessage(message);
        Assert.assertEquals("Test message", message.getTextMessage());
    }

    @Test
    void getAllMessagesByUserId() {
        User user = new User();
        user.setId(3L);
//        messageService.getAllIncomeMessagesByUserId(user.getId()).size();

        Assert.assertEquals(3, messageService.getAllIncomeMessagesByUserId(user.getId()).size());
    }
}