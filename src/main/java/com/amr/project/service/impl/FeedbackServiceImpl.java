package com.amr.project.service.impl;

import com.amr.project.dao.abstracts.FeedbackDao;
import com.amr.project.model.entity.Feedback;
import com.amr.project.service.abstracts.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl extends ReadWriteServiceImpl<Feedback, Long> implements FeedbackService {
  
    private final FeedbackDao feedbackDao;

    public FeedbackServiceImpl(FeedbackDao feedbackDao) {
        super(feedbackDao);
        this.feedbackDao = feedbackDao;
    }


    @Override
    public List<Feedback> printFeedback() {
        return feedbackDao.printFeedback();
    }

    @Override
    public void addFeedback(Feedback feedback) {
        feedbackDao.addFeedback(feedback);
    }

    @Override
    public Feedback findFeedbackById(Long id) {
        return feedbackDao.findFeedbackById(id);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long id) {
    feedbackDao.deleteFeedback(id);
    }
}
