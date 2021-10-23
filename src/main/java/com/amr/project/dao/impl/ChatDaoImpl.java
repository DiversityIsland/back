package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.ChatDao;
import com.amr.project.model.entity.Chat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatDaoImpl extends ReadWriteDAOImpl<Chat, Long> implements ChatDao {

    @Override
    public Chat findChatByHash(Long hash) {
        return entityManager.createQuery("SELECT c FROM Chat c WHERE c.hash = :hash", Chat.class)
                .setParameter("hash", hash)
                .getResultList().stream().findAny().orElse(null);
    }

    @Override
    public List<Chat> findChatsByUserId(Long id) {
        return entityManager.createQuery("SELECT c FROM Chat c LEFT JOIN c.members m WHERE m.id = :id", Chat.class)
                .setParameter("id", id)
                .getResultList();
    }


}
