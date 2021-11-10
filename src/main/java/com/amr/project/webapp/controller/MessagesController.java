package com.amr.project.webapp.controller;

import com.amr.project.model.entity.*;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.MessageService;
import com.amr.project.service.abstracts.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/messages")
public class MessagesController {

    private final UserService userService;
    private final MessageService messageService;
    private final ItemService itemService;

    @Autowired
    public MessagesController(UserService userService, MessageService messageService,
                              ItemService itemService) {
        this.userService = userService;
        this.messageService = messageService;
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public String showMessages(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userService.findByUsername(authentication.getName()).get();
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }

        User user = userService.findByUsername(authentication.getName()).get();

        List<Message> currentUserOutMessages = messageService.getAllMessagesByUserId(user.getId());
        List<Message> currentUserInMessages = messageService.getAllIncomeMessagesByUserId(user.getId());

        model.addAttribute("messages", currentUserOutMessages);
        model.addAttribute("messagesIn", currentUserInMessages);

        return "messages";
    }

    @PostMapping()
    public String addMessage(@ModelAttribute("messages") Message message,
                             @RequestParam(value = "itemId") Long id,
                             Principal principal) {

        User userTo = (User) Hibernate.unproxy(itemService.findItemById(id).getShop().getUser());

        message.setTo(userTo);
        message.setFrom(userService.findByUsername(principal.getName()).get());
        messageService.addMessage(message);

        return "redirect:/messages/list";
    }
}




