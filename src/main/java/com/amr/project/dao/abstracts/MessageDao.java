package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageDao extends ReadWriteDAO<Message, Long> {

    Optional<Message> getLastMessage();
    List<Message> findMessagesByChatId(Long id);
}
