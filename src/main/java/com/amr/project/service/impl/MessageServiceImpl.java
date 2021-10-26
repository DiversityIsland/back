package com.amr.project.service.impl;

import com.amr.project.dao.abstracts.MessageDao;
import com.amr.project.model.entity.Message;
import com.amr.project.service.abstracts.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl extends ReadWriteServiceImpl<Message, Long> implements MessageService {

    private final MessageDao messageDao;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao) {
        super(messageDao);
        this.messageDao = messageDao;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageDao.getAllMessages();
    }

    @Override
    public List<Message> getAllMessagesByUserId(Long id) {
        return messageDao.getAllMessagesByUserId(id);
    }

    @Override
    public List<Message> getAllIncomeMessagesByUserId(Long id) {
        return messageDao.getAllIncomeMessagesByUserId(id);
    }


    @Override
    public void addMessage(Message message) {
        messageDao.addMessage(message);
    }


}
