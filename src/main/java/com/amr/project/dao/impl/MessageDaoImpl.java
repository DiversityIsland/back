package com.amr.project.dao.impl;


import com.amr.project.dao.abstracts.MessageDao;
import com.amr.project.model.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageDaoImpl extends ReadWriteDAOImpl<Message, Long> implements MessageDao {


    @Override
    public Optional<Message> getLastMessage() {
        return entityManager.createQuery("SELECT m FROM Message m ORDER BY m.id DESC", Message.class)
                .getResultList().stream().findFirst();
    }

    @Override
    public List<Message> findMessagesByChatId(Long id) {
        return  entityManager.createQuery("SELECT m FROM Message m LEFT JOIN FETCH m.chat c WHERE c.id = :id", Message.class)
                .setParameter("id", id)
                .getResultList();
    }
}
