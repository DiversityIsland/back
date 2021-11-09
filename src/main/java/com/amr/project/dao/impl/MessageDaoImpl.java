package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.MessageDao;
import com.amr.project.model.entity.Message;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import static com.amr.project.dao.util.SingleResultUtil.getSingleResultOrNull;

@Repository
public class MessageDaoImpl extends ReadWriteDAOImpl<Message, Long> implements MessageDao {

    public List<Message> getAllMessagesByUserId(Long id) {
        return entityManager.createQuery("SELECT m FROM Message m WHERE m.from.id = :id", Message.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Message> getAllIncomeMessagesByUserId(Long id) {
        return entityManager.createQuery("SELECT m FROM Message m WHERE m.to.id = :id", Message.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public void addMessage(Message message) {
        entityManager.persist(message);
    }

    @Override
    public Optional<Message> getLastMessage() {
        return getSingleResultOrNull(entityManager.createQuery("SELECT m FROM Message m ORDER BY m.id DESC", Message.class));
    }

    @Override
    public List<Message> findMessagesByChatId(Long id) {
        return  entityManager.createQuery("SELECT m FROM Message m LEFT JOIN FETCH m.chat c WHERE c.id = :id", Message.class)
                .setParameter("id", id)
                .getResultList();
    }
}
