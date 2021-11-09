package com.amr.project.service.abstracts;

import com.amr.project.model.entity.CartItem;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface CartItemService extends ReadWriteService<CartItem, Long> {

    List<CartItem> findByUser(User user);

    List<CartItem> findByAnon(String anonID);

    void deleteByUserAndItem(Long userId, Long itemId);

    void updateQuantity(int quantity, Long userId, Long itemId);

    Optional<CartItem> findByItemAndShopAndUser(Long itemId, Long userId, Long shopId);

    Optional<CartItem> findByItemAndShopAndAnonID(Long itemId, Long shopId, String cookie);

    void updateUserToAnonCartItem(User user, String anonID);

}
