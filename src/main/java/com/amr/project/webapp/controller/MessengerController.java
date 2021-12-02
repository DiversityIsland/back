package com.amr.project.webapp.controller;

import com.amr.project.service.abstracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/messenger")
public class MessengerController {

    private final UserService userService;

    public MessengerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getMessengerPage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()).get());
        return "messenger";
    }
}
