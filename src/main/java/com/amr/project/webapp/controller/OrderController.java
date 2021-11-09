package com.amr.project.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order/{id}")
public class OrderController {

    @GetMapping
    public String orderPage(HttpServletRequest request) {
        System.out.println(request.getAttribute("test"));

        return "order_page";
    }
}
