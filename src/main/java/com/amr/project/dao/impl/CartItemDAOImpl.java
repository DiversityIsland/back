package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.CartItemDao;
import com.amr.project.dao.abstracts.UserDao;
import com.amr.project.model.entity.CartItem;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CartItemDAOImpl extends ReadWriteDAOImpl<CartItem, Long> implements CartItemDao {
    @Override
    public List<CartItem> findByUser(User user) {
        return entityManager.createQuery("SELECT c from CartItem c where c.user.username = :username", CartItem.class)
                .setParameter("username", user.getUsername()).getResultList();
    }

    @Override
    public List<CartItem> findByAnon(String anonID) {
        return entityManager.createQuery("SELECT c from CartItem c where c.anonID = :anonID", CartItem.class)
                .setParameter("anonID", anonID).getResultList();
    }


    @Override
    public void deleteByUserAndItem(Long userId, Long itemId) {
        entityManager.createQuery("delete from CartItem c where c.user.id = :userid and c.item.id = :itemid")
                .setParameter("userid", userId)
                .setParameter("itemid", itemId)
                .executeUpdate();
    }

    @Override
    public void updateQuantity(int quantity, Long userId, Long itemId) {
        entityManager.createQuery("update CartItem c set c.quantity=:newQuantity where c.user.id = :userid and c.item.id = :itemid")
                .setParameter("newQuantity", quantity)
                .setParameter("userid", userId)
                .setParameter("itemid", itemId)
                .executeUpdate();
    }

    @Override
    public Optional<CartItem> findByItemAndShopAndUser(Long itemId, Long userId, Long shopId) {
        return entityManager.createQuery("select c from CartItem c where c.user.id = :userid and c.item.id = :itemid " +
                "and c.shop.id= :shopid", CartItem.class)
                .setParameter("userid", userId)
                .setParameter("itemid", itemId)
                .setParameter("shopid", shopId)
                .getResultStream().findAny();
    }

    @Override
    public Optional<CartItem> findByItemAndShopAndAnonID(Long itemId, Long shopId, String anonID) {
        return entityManager.createQuery("select c from CartItem c where c.anonID = :anonID and c.item.id = :itemid " +
                        "and c.shop.id= :shopid", CartItem.class)
                .setParameter("anonID", anonID)
                .setParameter("itemid", itemId)
                .setParameter("shopid", shopId)
                .getResultStream().findAny();
    }

    @Override
    public void updateUserToAnonCartItem(User user, String anonID) {
        entityManager.createQuery("update CartItem c set c.user.id=:userID where c.anonID = :anonID")
                .setParameter("userID", user.getId())
                .setParameter("anonID", anonID)
                .executeUpdate();
    }
}
