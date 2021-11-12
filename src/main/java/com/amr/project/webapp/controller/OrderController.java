package com.amr.project.webapp.controller;

import com.amr.project.model.entity.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

//    @GetMapping("/order/{id}")
//    public String getOrder(@PathVariable("id") Long id, Model model) {
//        Item item = itemService.getByKey(id);
//        Shop shop = item.getShop();
//        model.addAttribute("item", itemMapper.itemToItemDto(item));
//        model.addAttribute("shop", shopMapper.shopToShopDto(shop));
//        model.addAttribute("reviews", itemService.getByKey(id).getReviews());
//        model.addAttribute("newReview", new Review());
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
//            User user = userService.findByUsername(authentication.getName()).get();
//            model.addAttribute("user", user);
//
//            model.addAttribute("discount", discountService.findByUserAndShop(user.getId(), item.getShop().getId()));
//            if (cartItemService.findByItemAndShopAndUser(id, user.getId(), item.getShop().getId()).isPresent()) {
//                CartItem cartItem = cartItemService.findByItemAndShopAndUser(id, user.getId(), item.getShop().getId()).get();
//                model.addAttribute("cartItem", cartItemMapper.cartItemToDto(cartItem));
//            }
//        }
//        return "product_page";
//    }
}
