package com.amr.project.webapp.controller;


import com.amr.project.model.entity.Feedback;
import com.amr.project.model.entity.User;
import com.amr.project.model.enums.FeedbackReason;
import com.amr.project.service.abstracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private UserService userService;
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(UserService userService,FeedbackService feedbackService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    @RequestMapping("/feedbacklist")
    public String showFeedback(Model model) {
        List<Feedback> feedbackList = feedbackService.printFeedback();
        model.addAttribute("feedback", feedbackList);
        return "feedbacklist";
    }

    @GetMapping()
    public String addFeedbackForm(Model model) {
        Feedback feedback = new Feedback();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
        feedback.setUsername(user.getUsername());
        model.addAttribute("feedback", feedback);
        model.addAttribute("FeedbackReasons",FeedbackReason.values());
        model.addAttribute("username",feedback.getUsername());

        return "feedback";
    }


    @PostMapping()
    public String addFeedback(@ModelAttribute("feedback") Feedback feedback) {
        feedbackService.addFeedback(feedback);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String deleteFeedback(@PathVariable(name = "id") Long id) {
        feedbackService.deleteFeedback(id);

        return "redirect:/";
    }
}
