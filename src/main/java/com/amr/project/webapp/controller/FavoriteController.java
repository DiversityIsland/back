package com.amr.project.webapp.controller;

import com.amr.project.model.dto.FavoriteDto;
import com.amr.project.model.entity.Favorite;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class FavoriteController {

    private final UserService userService;

    @Autowired
    public FavoriteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/favorites")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (authentication.isAuthenticated() && user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            model.addAttribute("user", new User());
        }
        return "favorites";
    }


    /*
    * список любимых товаров пользователя
    * запрашивать по юзернейму небезопасно, могут сбрутить базу юзернеймов
    * */
    @PostMapping("/favorites")
    public FavoriteDto getProfilePost(@RequestBody String userName) {
        //todo: контроль входного параметра userName. Вдруг там строка с хакерским кодом
        Optional<User> user = userService.findByUsername(userName);
        Favorite fav = user.map(User::getFavorite).orElse(new Favorite());
        return new FavoriteDto(fav);
    }

}