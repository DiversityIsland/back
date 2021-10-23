package com.amr.project.service.impl;

import com.amr.project.dao.abstracts.ChatDao;
import com.amr.project.dao.abstracts.ReadWriteDAO;
import com.amr.project.model.entity.Chat;
import com.amr.project.service.abstracts.ChatService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ChatServiceImpl extends ReadWriteServiceImpl<Chat, Long> implements ChatService {

    private final ChatDao chatDao;

    public ChatServiceImpl(ReadWriteDAO<Chat, Long> readWriteDAO, ChatDao chatDao) {
        super(readWriteDAO);
        this.chatDao = chatDao;
    }

    @Override
    public Chat findChatByHash(Long hash) {
        return chatDao.findChatByHash(hash);
    }

    @Override
    public List<Chat> findChatsByUserId(Long id) {
        return chatDao.findChatsByUserId(id);
    }

}
