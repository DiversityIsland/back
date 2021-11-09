package com.amr.project.webapp.controller;


import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.CategoryService;
import com.amr.project.service.abstracts.MainPageItemService;
import com.amr.project.service.abstracts.MainPageShopService;
import com.amr.project.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainPageController {
    private final MainPageItemService mainPageItemService;
    private final MainPageShopService mainPageShopService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public MainPageController(MainPageItemService mainPageItemService, MainPageShopService mainPageShopService,
                              UserService userService, CategoryService categoryService) {
        this.mainPageItemService = mainPageItemService;
        this.mainPageShopService = mainPageShopService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    /**
     * Этот метод используется для отображения home.html
     * В Thymeleaf передаются популярные Items и Shops + Categoryes
     *
     * @param model
     * @return
     */
    @GetMapping
    public String getPopularItemsAndShop(Model model) throws UnsupportedEncodingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if (authentication.isAuthenticated() && user.isPresent()) {
            model.addAttribute("user", user.get());
            if (!user.get().isUsingTwoFactorAuth()) {
                String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M|0&cht=qr&chl=otpauth://totp/";
                String qr = QR_PREFIX + URLEncoder.encode(
                        String.format("%s:%s?secret=%s&issuer=%s",
                                "Platform", user.get().getUsername(), user.get().getSecret(), "Platform"),
                        StandardCharsets.UTF_8);
                model.addAttribute("qr", qr);
            }
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("items", mainPageItemService.findPopularItems());
        model.addAttribute("shops", mainPageShopService.findPopularShops());
        model.addAttribute("categories", categoryService.getCategoryDto());
        return "home";
    }

    /**
     * Метод используется для получения Item по id категории на фронте
     *
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    @ResponseBody
    public ResponseEntity<List<ItemDto>> findItemsByCategoryId(@PathVariable Long id) {
        return new ResponseEntity<>(mainPageItemService.findItemsByCategoryId(id),
                HttpStatus.OK);
    }

}
