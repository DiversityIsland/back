package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.ItemMapper;
import com.amr.project.converter.OrderMapper;
import com.amr.project.model.dto.AddressDto;
import com.amr.project.model.dto.CartItemDto;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.dto.OrderDto;
import com.amr.project.model.entity.Order;
import com.amr.project.model.entity.User;
import com.amr.project.model.enums.Status;
import com.amr.project.service.abstracts.ItemService;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private final ItemService itemService;

    @Autowired
    public OrderRestController(OrderService orderService,
                               OrderMapper orderMapper,
                               ItemMapper itemMapper,
                               UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemMapper = itemMapper;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
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


    @PutMapping("/start/{orderId}")
        public ResponseEntity<?> setStatusStart(@PathVariable("orderId") String id) {

            try {
                Long itemId = Long.parseLong(id);
                itemService.SetStartItemsByShopId(itemId);
            }catch (Exception e){
                LOGGER.warn("Произошла непредвиденная ошибка при смене статуса заказа!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            LOGGER.info("Заказ " + id + " перемещен в статус START");
            return new ResponseEntity(HttpStatus.OK);


        }

        @PutMapping("/complete/{orderId}")
        public ResponseEntity<?> setStatusComplete(@PathVariable("orderId") String id) {
            try {
                Long itemId = Long.parseLong(id);
                itemService.SetCompleteItemsByShopId(itemId);
            }catch (Exception e){
                LOGGER.warn("Произошла непредвиденная ошибка при смене статуса заказа!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            LOGGER.info("Заказ " + id + " перемещен в статус COMPLETE");
            return new ResponseEntity(HttpStatus.OK);


        }

        @PutMapping("/sent/{orderId}")
        public ResponseEntity<?> setStatusSent(@PathVariable("orderId") String id) {
            try {
                Long itemId = Long.parseLong(id);
                itemService.SetSentItemsByShopId(itemId);
            }catch (Exception e){
                LOGGER.warn("Произошла непредвиденная ошибка при смене статуса заказа!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            LOGGER.info("Заказ " + id + " перемещен в статус SENT");
            return new ResponseEntity(HttpStatus.OK);



        }

        @PutMapping("/delivered/{orderId}")
        public ResponseEntity<?> setStatusDelivered(@PathVariable("orderId") String id) {
            try {
                Long itemId = Long.parseLong(id);
                itemService.SetDeliveredItemsByShopId(itemId);
            }catch (Exception e){
                LOGGER.warn("Произошла непредвиденная ошибка при смене статуса заказа!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            LOGGER.info("Заказ " + id + " перемещен в статус DELIVERED");
            return new ResponseEntity(HttpStatus.OK);



        }
}
