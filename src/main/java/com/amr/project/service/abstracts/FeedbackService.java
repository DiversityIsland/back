package com.amr.project.service.abstracts;


import com.amr.project.model.entity.Feedback;


import java.util.List;

public interface FeedbackService extends ReadWriteService<Feedback, Long>{
     List<Feedback> printFeedback();

     void addFeedback(Feedback feedback);

     Feedback findFeedbackById(Long id);

     void deleteFeedback(Long id);


}
