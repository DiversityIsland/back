package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.FeedbackDao;
import com.amr.project.model.entity.Feedback;
import com.amr.project.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedbackDaoImpl extends ReadWriteDAOImpl<Feedback, Long> implements FeedbackDao {
    @Override
    public List<Feedback> printFeedback() {
         return entityManager.createQuery("select F FROM Feedback F", Feedback.class).getResultList();
    }
    @Override
    public Feedback findFeedbackById(Long id) {
        return entityManager.createQuery("SELECT f from Feedback f where f.id = :id", Feedback.class)
                .setParameter("id", id).getResultList()
                .stream()
                .findFirst().orElse(new Feedback());
    }

    @Override
    public void addFeedback(Feedback feedback) {
        entityManager.persist(feedback);
    }


    @Override
    public void deleteFeedback(Long id) {
        entityManager.remove(findFeedbackById(id));
    }

    @Override
    public Optional<Feedback> findByUser(User user) {
        return entityManager.createQuery("select f from Feedback f where f.username= :username", Feedback.class)
                .setParameter("username", user.getUsername())
                .getResultStream().findAny();
    }
}
