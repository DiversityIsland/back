package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.Chat;

import java.util.List;

public interface ChatDao extends ReadWriteDAO<Chat, Long>{
    Chat findChatByHash(Long hash);
    List<Chat> findChatsByUserId(Long id);
}
