package com.amr.project.service.impl;

import com.amr.project.dao.abstracts.MessageDao;
import com.amr.project.dao.abstracts.ReadWriteDAO;
import com.amr.project.model.entity.Message;
import com.amr.project.service.abstracts.MessageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageServiceImpl extends ReadWriteServiceImpl<Message, Long> implements MessageService {

    private final MessageDao messageDao;

    public MessageServiceImpl(ReadWriteDAO<Message, Long> readWriteDAO, MessageDao messageDao) {
        super(readWriteDAO);
        this.messageDao = messageDao;
    }


    @Override
    public Optional<Message> getLastMessage() {
        return messageDao.getLastMessage();
    }

    @Override
    public List<Message> findMessagesByChatId(Long id) {
        return messageDao.findMessagesByChatId(id);
    }
}
