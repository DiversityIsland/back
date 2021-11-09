package com.amr.project.dao.abstracts;

import com.amr.project.model.entity.Favorite;
import com.amr.project.model.entity.Feedback;
import com.amr.project.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedbackDao extends ReadWriteDAO<Feedback, Long>{


     List<Feedback> printFeedback();

     Feedback findFeedbackById(Long id);

     void addFeedback(Feedback feedback);

     void deleteFeedback(Long id);

     Optional<Feedback> findByUser(User user);

}
