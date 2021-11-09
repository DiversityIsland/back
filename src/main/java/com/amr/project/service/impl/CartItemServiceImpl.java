package com.amr.project.service.impl;

import com.amr.project.dao.abstracts.CartItemDao;
import com.amr.project.dao.abstracts.RoleDao;
import com.amr.project.model.entity.CartItem;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Role;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.CartItemService;
import com.amr.project.service.abstracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl extends ReadWriteServiceImpl<CartItem, Long> implements CartItemService {

    private final CartItemDao cartItemDao;

    @Autowired
    public CartItemServiceImpl(CartItemDao cartItemDao) {
        super(cartItemDao);
        this.cartItemDao = cartItemDao;
    }

    @Override
    public List<CartItem> findByUser(User user) {
        return cartItemDao.findByUser(user);
    }

    @Override
    public List<CartItem> findByAnon(String anonID) {
        return cartItemDao.findByAnon(anonID);
    }


    @Override
    public void deleteByUserAndItem(Long userId, Long itemId) {
        cartItemDao.deleteByUserAndItem(userId, itemId);
    }

    @Override
    public void updateQuantity(int quantity, Long userId, Long itemId) {
        cartItemDao.updateQuantity(quantity, userId, itemId);
    }

    @Override
    public Optional<CartItem> findByItemAndShopAndUser(Long itemId, Long userId, Long shopId) {
        return cartItemDao.findByItemAndShopAndUser(itemId, userId, shopId);
    }

    @Override
    public Optional<CartItem> findByItemAndShopAndAnonID(Long itemId, Long shopId, String cookie) {
        return cartItemDao.findByItemAndShopAndAnonID(itemId, shopId, cookie);
    }

    @Override
    public void updateUserToAnonCartItem(User user, String anonID) {
        cartItemDao.updateUserToAnonCartItem(user, anonID);
    }


}
