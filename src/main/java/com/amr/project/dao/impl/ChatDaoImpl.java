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
        return entityManager.createQuery("SELECT c FROM Chat c JOIN c.members m WHERE m.id = :id", Chat.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Chat findChatByToUserIdAndFromUserId(Long toId, Long fromId) {
        String query = "SELECT * FROM chat c JOIN chat_members cm " +
                "ON (c.id = cm.chat_id AND cm.members_id = :fromId) " +
                "WHERE c.id IN (SELECT c2.id FROM chat c2 JOIN chat_members cm2 " +
                "ON c2.id = cm2.chat_id AND cm2.members_id = :toId)";

        return (Chat) entityManager.createNativeQuery(query, Chat.class)
                .setParameter("fromId", fromId)
                .setParameter("toId", toId)
                .getResultStream().findFirst().orElse(new Chat());
    }
}