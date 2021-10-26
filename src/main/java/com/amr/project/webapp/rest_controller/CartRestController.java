package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.CartItemMapper;
import com.amr.project.converter.UserMapper;
import com.amr.project.model.dto.CartItemDto;
import com.amr.project.model.entity.CartItem;
import com.amr.project.model.entity.Favorite;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private final CartItemService cartItemService;
    private final UserService userService;
    private final ItemService itemService;
    private final CartItemMapper cartItemMapper;
    private final UserMapper userMapper;
    private final DiscountService discountService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CartRestController(CartItemService cartItemService, UserService userService,
                              ItemService itemService, CartItemMapper cartItemMapper,
                              UserMapper userMapper, DiscountService discountService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.itemService = itemService;
        this.cartItemMapper = cartItemMapper;
        this.userMapper = userMapper;
        this.discountService = discountService;
    }

    @Transactional
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartItemDto>> getAllCartItemsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("Вам нужно авторизоваться для доступа к корзине");
        }
        User user = userService.findByUsername(authentication.getName()).get();
        List<CartItem> cartItems = cartItemService.findByUser(user);
        List<CartItemDto> cartItemsDto = cartItems.stream().map(c -> cartItemMapper.cartItemToDto(c)).collect(Collectors.toList());
        LOGGER.info(String.format("Пользователь с id %d успешно получил список товаров в корзине", user.getId()));
        return ResponseEntity.ok(cartItemsDto);
    }

    @Transactional
    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable("id") Long id, @RequestBody CartItemDto cartItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("Вам нужно авторизоваться для доступа к корзине");
        }
        cartItemService.getByKey(id).setQuantity(cartItem.getQuantity());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("Вам нужно авторизоваться для доступа к корзине");
        }
        cartItemService.deleteByKeyCascadeIgnore(id);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping(value = "/add")
    public ResponseEntity<Void> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Вам нужно авторизоваться для доступа к корзине");
        }
        User user = userService.findByUsername(authentication.getName()).get();
        cartItemDto.setUser(userMapper.userToDto(user));

        CartItem cartItem;
        Optional<CartItem> cartItem1 = cartItemService.findByItemAndShopAndUser(
                cartItemDto.getItem().getId(),
                cartItemDto.getUser().getId(),
                cartItemDto.getShop().getId());

        if(cartItem1.isPresent()) {
            cartItem = cartItem1.get();
            cartItemDto.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
            updateCartItemQuantity(cartItem.getId(), cartItemDto);
        } else {
            cartItem = cartItemMapper.dtoToCartItem(cartItemDto);
            cartItemService.persist(cartItem);
            LOGGER.info(String.format("Пользователь с id %d успешно добавил товар с id %d в корзину", cartItem.getUser().getId(),
                    cartItem.getId()));
        }
        return ResponseEntity.ok().build();
    }
}