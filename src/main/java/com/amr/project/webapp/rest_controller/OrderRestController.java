package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.ItemMapper;
import com.amr.project.converter.OrderMapper;
import com.amr.project.model.dto.AddressDto;
import com.amr.project.model.dto.CartItemDto;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.dto.OrderDto;
import com.amr.project.model.entity.Order;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.OrderService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Autowired
    public OrderRestController(OrderService orderService,
                               OrderMapper orderMapper,
                               ItemMapper itemMapper,
                               UserService userService) {
        this.userService = userService;
        this.itemMapper = itemMapper;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") Long orderId) {
        LOGGER.info("Пользователь получил заказ с id" + orderId.toString());
        return new ResponseEntity<>(orderMapper.orderToDto(orderService.getByKey(orderId)),
                HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<OrderDto> saveOrder(@RequestBody List<CartItemDto> items) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOp = userService.findByUsername(authentication.getName());

        if(authentication.isAuthenticated() && userOp.isPresent()) {
            Order order = orderService.collectOrderByUserAndItems(items.stream().map(CartItemDto::getItem).collect(Collectors.toList()), userOp.get());
            LOGGER.info("Пользователь создал заказ с id = " + order.getId().toString());
            return new ResponseEntity<>(orderMapper.orderToDto(order),
                    HttpStatus.OK);
        } else {
            LOGGER.warn("Ошибка при создании заказа");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable("orderId") Long id, @RequestBody OrderDto orderDto) {
        orderService.updateAddressAndUserInfo(id, orderDto);
        LOGGER.info("Пользователь обновил заказ с id = " + id.toString());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/deleteItem/{orderId}/{itemId}")
    public ResponseEntity<?> deleteItemInOrder(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {
        orderService.deleteItemInOrder(orderId, itemId);
        LOGGER.info("Пользователь удалил итем с id = " + itemId.toString() + " из заказа с id = " + orderId.toString());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> delete(@PathVariable("orderId") Long id) {
        orderService.deleteByKeyCascadeIgnore(id);
        LOGGER.info("Пользователь удалил заказ с id = " + id.toString());
        return ResponseEntity.noContent().build();
    }
}
