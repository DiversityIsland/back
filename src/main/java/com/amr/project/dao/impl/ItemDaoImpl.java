package com.amr.project.dao.impl;


import com.amr.project.dao.abstracts.ItemDao;
import com.amr.project.model.entity.Item;
import com.amr.project.model.enums.Status;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ItemDaoImpl extends ReadWriteDAOImpl<Item, Long> implements ItemDao {


    @Override
    public Item findItemById(Long id) {
        return entityManager.createQuery("SELECT i from Item i where i.id = :id", Item.class)
                .setParameter("id", id).getResultList()
                .stream()
                .findFirst().orElse(new Item());
    }

    @Override
    public Item findItemByName(String name) {
        return entityManager.createQuery("SELECT i from Item i where i.name = :name", Item.class)
                .setParameter("name", name).getResultList()
                .stream()
                .findFirst().orElse(new Item());
    }

    @Override
    public List<Item> getItemsByShopId(Long id) {

        String HQL = "SELECT i FROM Item i LEFT JOIN FETCH i.shop s WHERE " +
                "s.id =:id ";

        TypedQuery<Item> query = entityManager.createQuery(HQL, Item.class);
        query.setParameter("id", id);


        return query.getResultList();
    }

    //Меняет статус на START
    @Override
    public void setStartItemsByShopId(Long id) {
        int status = 2;
        entityManager.createQuery("update Order o set o.status =: status where o.id =: id")

                .setParameter("status", Status.START)
                .setParameter("id", id)
                .executeUpdate();
    }
    //Меняет статус на COMPLETE
    @Override
    public void setCompleteItemsByShopId(Long id) {
         entityManager.createQuery("update Order o set o.status =: status where o.id =: id")
                .setParameter("id", id)
                .setParameter("status", Status.COMPLETE)
                .executeUpdate();
    }


    @Override
    public List<Item> getSoldItemsByShopId(Long id) {

        String query = "SELECT * FROM item JOIN orders_item oi on item.id = oi.item_id " +
                "JOIN orders o on oi.orders_id = o.id JOIN shop_item si on item.id = si.item_id " +
                "JOIN shop s on si.shop_id = s.id WHERE o.status = :status AND s.id = :id";



        return entityManager. createNativeQuery(query)
                .setParameter("id", id)
                .setParameter("status", Status.COMPLETE.ordinal())
                .getResultList();
    }

    //Меняет статус на SENT
    @Override
    public void setSentItemsByShopId(Long id) {

         entityManager.createQuery("update Order o set o.status =: status where o.id =: id")
                .setParameter("id", id)
                .setParameter("status", Status.SENT)
                .executeUpdate();
    }

    //Меняет статус на DELIVERED
    @Override
    public void setDeliveredItemsByShopId(Long id) {
         entityManager.createQuery("update Order o set o.status =: status where o.id =: id")
                .setParameter("id", id)
                .setParameter("status", Status.DELIVERED)
                .executeUpdate();
    }


    @Override
    public List<Item> getUnmoderatedItems() {
        return entityManager.createQuery("SELECT i from Item i where i.isModerateAccept = false and i.isModerated = false ", Item.class)
                .getResultList();
    }

    @Override
    public List<Item> getModeratedItems() {
        return entityManager.createQuery("SELECT i from Item i where i.isModerateAccept = true and i.isModerated = true ", Item.class)
                .getResultList();
    }
}

