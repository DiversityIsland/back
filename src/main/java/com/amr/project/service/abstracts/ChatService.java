package com.amr.project.service.abstracts;

import com.amr.project.model.entity.Chat;

import java.util.List;

public interface ChatService extends ReadWriteService<Chat, Long> {
    Chat findChatByHash(Long hash);

    List<Chat> findChatsByUserId(Long id);

    Chat findChatByToUserIdAndFromUserId(Long toId, Long fromId);
}
