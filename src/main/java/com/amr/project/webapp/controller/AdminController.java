package com.amr.project.webapp.controller;


import com.amr.project.service.abstracts.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {


    @GetMapping("/admin")
    public String registerUser() {
        System.out.println("========================== \\admin ===========================");
        return "admin_panel";
    }


}
