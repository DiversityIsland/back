package com.amr.project.service.abstracts;

import com.amr.project.model.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService extends ReadWriteService<Message, Long> {
    Optional<Message> getLastMessage();

    List<Message> findMessagesByChatId(Long id);
}
