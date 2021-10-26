package com.amr.project.webapp.controller;

import com.amr.project.converter.CartItemMapper;
import com.amr.project.converter.ItemMapper;
import com.amr.project.converter.ShopMapper;
import com.amr.project.model.entity.*;
import com.amr.project.service.abstracts.CartItemService;
import com.amr.project.service.abstracts.DiscountService;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.UserService;
import com.amr.project.service.abstracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ItemController {

    private ItemService itemService;
    private CartItemService cartItemService;
    private UserService userService;
    private ItemMapper itemMapper;
    private CartItemMapper cartItemMapper;
    private ShopMapper shopMapper;
    private DiscountService discountService;
    private final ReviewService reviewService;

    @Autowired
    public ItemController(ItemService itemService, CartItemService cartItemService, UserService userService,
                          ItemMapper itemMapper, CartItemMapper cartItemMapper, ShopMapper shopMapper,
                          DiscountService discountService, ReviewService reviewService) {
        this.itemService = itemService;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.itemMapper = itemMapper;
        this.cartItemMapper = cartItemMapper;
        this.shopMapper = shopMapper;
        this.discountService = discountService;
        this.reviewService = reviewService;
    }


    @GetMapping("/product/{id}")
    public String itemById(@PathVariable("id") Long id, Model model) {
        Item item = itemService.getByKey(id);
        Shop shop = item.getShop();
        model.addAttribute("item", itemMapper.itemToItemDto(item));
        model.addAttribute("shop", shopMapper.shopToShopDto(shop));
        model.addAttribute("reviews", itemService.getByKey(id).getReviews());
        model.addAttribute("newReview", new Review());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
             User user = userService.findByUsername(authentication.getName()).get();
             model.addAttribute("user", user);

            model.addAttribute("discount", discountService.findByUserAndShop(user.getId(), item.getShop().getId()));
            if (cartItemService.findByItemAndShopAndUser(id, user.getId(), item.getShop().getId()).isPresent()) {
                CartItem cartItem = cartItemService.findByItemAndShopAndUser(id, user.getId(), item.getShop().getId()).get();
                model.addAttribute("cartItem", cartItemMapper.cartItemToDto(cartItem));
            }
        }
        return "product_page";
    }

    @PostMapping("/product/{id}")
    public String saveReview(@PathVariable("id") Long id,
                             @ModelAttribute("newReview") Review review,
                             @ModelAttribute("rating") int rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
        review.setRating(rating);
        review.setDate(new java.sql.Date(System.currentTimeMillis()));
        review.setId(null);
        review.setItem(itemService.getByKey(id));
        review.setUser(user);
        reviewService.persist(review);
        return "redirect:/product/" + id;
    }
}