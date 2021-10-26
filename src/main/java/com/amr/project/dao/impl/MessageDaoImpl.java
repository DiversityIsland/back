package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.MessageDao;
import com.amr.project.model.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDaoImpl extends ReadWriteDAOImpl<Message, Long> implements MessageDao {
    @Override
    public List<Message> getAllMessages() {
        return entityManager.createQuery("SELECT m FROM Message m", Message.class)
                .getResultList();
    }

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



}
