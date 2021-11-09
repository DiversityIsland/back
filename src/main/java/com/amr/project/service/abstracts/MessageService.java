package com.amr.project.service.abstracts;

import com.amr.project.model.entity.Message;

import java.util.List;

public interface MessageService extends ReadWriteService<Message, Long> {

    List<Message> getAllMessagesByUserId(Long id);

    List<Message> getAllIncomeMessagesByUserId(Long id);

    void addMessage(Message message);
}
