package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.Message;

import java.util.List;

public interface MessageDao extends ReadWriteDAO<Message, Long> {

    List<Message> getAllMessages();

    List<Message> getAllMessagesByUserId(Long id);

    List<Message> getAllIncomeMessagesByUserId(Long id);

    void addMessage(Message message);

//    Optional<Message> findUserById(Long id);
}
