package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.DiscountMapper;
import com.amr.project.model.dto.DiscountDto;
import com.amr.project.model.entity.Discount;
import com.amr.project.model.entity.Favorite;
import com.amr.project.model.entity.Shop;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/discounts")
public class DiscountRestController {

    private final UserService userService;
    private final DiscountService discountService;
    private final ShopService shopService;
    private final DiscountMapper discountMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DiscountRestController(UserService userService, DiscountService discountService,
                                  DiscountMapper discountMapper,
                                  ShopService shopService) {
        this.userService = userService;
        this.discountService = discountService;
        this.shopService = shopService;
        this.discountMapper = discountMapper;
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<DiscountDto> getDiscountByShopIdAndUserId(@PathVariable("shopId") Long shopId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOp = userService.findByUsername(authentication.getName());

        if(authentication.isAuthenticated() && userOp.isPresent()) {
            return ResponseEntity.ok(discountService.findByUserAndShop(userOp.get().getId(), shopId));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<DiscountDto>> getDiscounts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("Вам нужно авторизоваться для доступа к списку скидок");
        }
        User user = userService.findByUsername(authentication.getName()).get();
        List<DiscountDto> discounts = discountService.findByUser(user);
        return ResponseEntity.ok().body(discounts);
    }
    @GetMapping("/get_owned_shops")
    public ResponseEntity<String[]> getOwnedShops(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("Вам нужно авторизоваться для создания скидки");
        }
        User shopOwner = userService.findByUsername(authentication.getName()).get();
        Collection<Shop> shops = shopOwner.getShops();
        LOGGER.info(String.format("Пользователь с id %d получил список магазинов для установки скидки", shopOwner.getId()));
        if (shops.isEmpty()) {
            return ResponseEntity.ok().body(new String[]{});
        } else {
            String[] shopsString = shops.stream().map(s -> s.getName()).toArray(String[]::new);
            return ResponseEntity.ok().body(shopsString);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<Void> createDiscount(@RequestBody DiscountDto discountDto, @RequestParam String shopname,
                                               @RequestParam String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("Вам нужно авторизоваться для создания скидки");
        }
        User shopOwner = userService.findByUsername(authentication.getName()).get();
        Shop shop = shopService.getByName(shopname);
        Optional<User> user = userService.findByUsername(username);;
        if (!user.isPresent()) {
            LOGGER.info(String.format("Ошибка добавления скидки, пользователь с указанным юзернеймом %s не был найден", username));
            return ResponseEntity.badRequest().body(null);
        }
        discountDto.setShopId(shop.getId());
        discountDto.setUserId(user.get().getId());
        discountService.persist(discountMapper.fromDto(discountDto));
        LOGGER.info(String.format("Пользователь с id %d успешно создал скидку", shopOwner.getId()));
        return ResponseEntity.ok().body(null);
    }
}
