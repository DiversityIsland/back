package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.AddressMapper;
import com.amr.project.converter.UserMapper;
import com.amr.project.model.dto.OrderDto;
import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.dto.UserDto;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.UserPageOrderService;
import com.amr.project.service.abstracts.UserPageShopService;
import com.amr.project.service.abstracts.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/userPage")
public class UserPageRestController {
    private final UserPageShopService userPageShopService;
    private final UserPageOrderService userPageOrderService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final ItemService itemService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserPageRestController(UserPageOrderService userPageOrderService,
                                  UserPageShopService userPageShopService,
                                  UserService userService,
                                  UserMapper userMapper,
                                  AddressMapper addressMapper,
                                  ItemService itemService) {
        this.itemService = itemService;
        this.addressMapper = addressMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userPageOrderService = userPageOrderService;
        this.userPageShopService = userPageShopService;

    }
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUsername(authentication.getName());
        if(authentication.isAuthenticated() && user.isPresent()) {
            return new ResponseEntity<>(userMapper.userToDto(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/updatePassword/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        User user = userService.getByKey(id);
        user.setPassword(userDto.getPassword());
        userService.update(user);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        if(userDto != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> userOp = userService.findByUsername(authentication.getName());
            if(authentication.isAuthenticated() && userOp.isPresent()) {
               User user = userOp.get();
               userService.updateUserOnUserPage(user, userDto);
            }

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrdersByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOp = userService.findByUsername(authentication.getName());
        if(authentication.isAuthenticated() && userOp.isPresent()) {
            User user = userOp.get();
            logger.info("Пользователь с id = " + user.getId().toString() + " получил список своих заказов");
            //проверяем стутус оплаты через Qiwi Api
            userPageOrderService.checkQiwiPaymentStatus(user.getId());
            return new ResponseEntity<>(userPageOrderService.getOrdersByUserId(user.getId()), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/shops")
    public ResponseEntity<List<ShopDto>> getShopsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOp = userService.findByUsername(authentication.getName());
        if(authentication.isAuthenticated() && userOp.isPresent()) {
            User user = userOp.get();
            logger.info("Пользователь с id = " + user.getId().toString() + " получил список своих магазинов");
            return new ResponseEntity<>(userPageShopService.getShopsByUserId(user.getId()), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/deleteShop/{id}")
    public ResponseEntity<?> makeShopPretendentToBeDeletedById(@PathVariable Long id) {
        userPageShopService.makeShopPretendentToBeDeletedById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/deleteItem/{id}")
    public ResponseEntity<?> makeItemPretendentToBeDeletedById(@PathVariable Long id) {
        itemService.makeItemPretendentToBeDeletedById(id);
        return ResponseEntity.noContent().build();
    }

}
