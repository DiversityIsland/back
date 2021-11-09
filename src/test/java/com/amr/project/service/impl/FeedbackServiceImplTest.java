package com.amr.project.service.impl;

import com.amr.project.model.entity.Feedback;
import com.amr.project.model.enums.FeedbackReason;
import com.amr.project.service.abstracts.FeedbackService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackServiceImplTest {

    @Autowired
    FeedbackService feedbackService;

    private Feedback feedback;

    @Before
    public void createEntityForTest(){
        feedback = new Feedback();
        feedback.setUsername("Nagibator2000");
        feedback.setFull_text("Где деньги, Лебовски?");
        feedback.setReason(FeedbackReason.QUESTION.getName());

    }

    @Test
    public void createFeedback(){
        feedbackService.addFeedback(feedback);

        assertEquals("Nagibator2000", feedback.getUsername());
        assertEquals("Где деньги, Лебовски?", feedback.getFull_text());
        assertEquals("Вопрос",FeedbackReason.QUESTION.getName());
    }

    @Test
    public void deleteFeedback(){
        feedbackService.deleteFeedback(feedback.getId());
        Feedback deletedFeedback = feedbackService.findFeedbackById(feedback.getId());
        assertNull(deletedFeedback.getDateTime());
        assertNull(deletedFeedback.getUsername());
        assertNull(deletedFeedback.getId());
        assertNull(deletedFeedback.getReason());
        assertNull(deletedFeedback.getFull_text());
    }

}
