package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.ChatDao;
import com.amr.project.model.entity.Chat;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.amr.project.dao.util.SingleResultUtil.getSingleResultOrNull;

@Repository
public class ChatDaoImpl extends ReadWriteDAOImpl<Chat, Long> implements ChatDao {

    @Override
    public Chat findChatByHash(Long hash) {
        return getSingleResultOrNull(entityManager.createQuery("SELECT c FROM Chat c WHERE c.hash = :hash", Chat.class)
                .setParameter("hash", hash)).orElse(null);
    }

    @Override
    public List<Chat> findChatsByUserId(Long id) {
        return entityManager.createQuery("SELECT c FROM Chat c JOIN c.members m WHERE m.id = :id", Chat.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Chat findChatByToUserIdAndFromUserId(Long toId, Long fromId) {
        String query = "SELECT c FROM Chat c JOIN c.members m ON m.id = :fromId " +
                "WHERE c.id IN (SELECT c2.id FROM Chat c2 JOIN c2.members m2 ON m2.id = :toId)";

        return getSingleResultOrNull(entityManager.createQuery(query, Chat.class)
                .setParameter("fromId", fromId)
                .setParameter("toId", toId))
                .orElse(new Chat());
    }
}