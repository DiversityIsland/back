package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.CartItemMapper;
import com.amr.project.converter.UserMapper;
import com.amr.project.model.dto.CartItemDto;
import com.amr.project.model.entity.CartItem;
import com.amr.project.model.entity.Favorite;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.*;
import org.apache.http.HttpRequest;
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
import springfox.documentation.service.ApiListing;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
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
    public ResponseEntity<List<CartItemDto>> getAllCartItemsByUser(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
           Cookie[] cookie = request.getCookies();
           
           if (cookie != null) {
               for (Cookie c : cookie) {
                   if (c.getName().equals("anonItemCartID")) {
                        // Если в корзине есть товары от анонима, то вывести их.
                       List<CartItem> cartItems = cartItemService.findByAnon(c.getValue());
                       List<CartItemDto> cartItemsDto = cartItems.stream().map(a -> cartItemMapper.cartItemToDto(a)).collect(Collectors.toList());

                       return ResponseEntity.ok(cartItemsDto);
                   }
               }
           }
//        }
        User user = userService.findByUsername(authentication.getName()).get();
        List<CartItem> cartItems = cartItemService.findByUser(user);
        List<CartItemDto> cartItemsDto = cartItems.stream().map(c -> cartItemMapper.cartItemToDto(c)).collect(Collectors.toList());
        LOGGER.info(String.format("Пользователь с id %d успешно получил список товаров в корзине", user.getId()));

        return ResponseEntity.ok(cartItemsDto);
    }

    @Transactional
    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable("id") Long id, @RequestBody CartItemDto cartItem) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
//            throw new AccessDeniedException("Вам нужно авторизоваться для доступа к корзине");
//        }
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
    public ResponseEntity<Void> addItemToCart(@RequestBody CartItemDto cartItemDto, HttpServletRequest request,
                                              HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartItem cartItem;

        // Делаем проверку прошел ли пользователь авторизацию, если нет то ...
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            //Надо првоерить есть ли уже в БД корзина с таким anonID.
            String anonID = request.getSession().getId();
            response.addCookie(new Cookie("anonItemCartID", anonID)); //добавляем anonID в куки

            Optional<CartItem> cartItem1 = cartItemService.findByItemAndShopAndAnonID(
                    cartItemDto.getItem().getId(),
                    cartItemDto.getShop().getId(),
                    anonID);

            if (cartItem1.isPresent()) {
                cartItem = cartItem1.get(); // Если в корзине уже есть товар, значит Optional будет не null и вернет cartItem
                cartItemDto.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
                updateCartItemQuantity(cartItem.getId(), cartItemDto);
            } else {
                cartItem = cartItemMapper.dtoToCartItem(cartItemDto);
                cartItem.setAnonID(anonID);
                cartItemService.persist(cartItem);

                LOGGER.info(String.format("Анонимный пользователь успешно добавил товар с id %d в корзину", cartItem.getId(),
                        cartItem.getId()));
            }
        } else {    // если пользователь зарегистрирован
            User user = userService.findByUsername(authentication.getName()).get();
            cartItemDto.setUser(userMapper.userToDto(user));

            Optional<CartItem> cartItem1 = cartItemService.findByItemAndShopAndUser(
                    cartItemDto.getItem().getId(),
                    cartItemDto.getUser().getId(),
                    cartItemDto.getShop().getId());

            if (cartItem1.isPresent()) {
                cartItem = cartItem1.get(); // Если в корзине уже есть товар, значит Optional будет не null и вернет cartItem
                cartItemDto.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
                updateCartItemQuantity(cartItem.getId(), cartItemDto);
            } else {
                //надо проверить null или нет anonID если да то значт юзер перед тем как войти в профиль товар в корзину не добавлял.
                Cookie[] cookie = request.getCookies();

                if (cookie != null) {
                    for (Cookie c : cookie) {
                        if (c.getName().equals("anonItemCartID")) {
                            cartItemService.updateUserToAnonCartItem(user, c.getValue());//переименовать

                            Optional<CartItem> cartItem2 = cartItemService.findByItemAndShopAndUser(
                                    cartItemDto.getItem().getId(),
                                    cartItemDto.getUser().getId(),
                                    cartItemDto.getShop().getId());
                            if (cartItem2.isPresent()) {
                                cartItem = cartItem2.get();
                                cartItemDto.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
                                updateCartItemQuantity(cartItem.getId(), cartItemDto);

                            } else { // если зарегистрированный первый раз добавил новый товар
                                cartItem = cartItemMapper.dtoToCartItem(cartItemDto);
                                cartItemService.persist(cartItem);

                            }
                            c.setMaxAge(0);
                            response.addCookie(c);
                            return ResponseEntity.ok().build();
                        }
                    }
                }
                cartItem = cartItemMapper.dtoToCartItem(cartItemDto);
                cartItemService.persist(cartItem);

                LOGGER.info(String.format("Пользователь с id %d успешно добавил товар с id %d в корзину", cartItem.getUser().getId(),
                        cartItem.getId()));
            }
        }
        return ResponseEntity.ok().build();
    }
}