package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.MainPageItemsDao;
import com.amr.project.dao.util.SingleResultUtil;
import com.amr.project.model.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.amr.project.dao.util.SingleResultUtil.getSingleResultOrNull;

@Repository
public class MainPageItemsDaoImpl extends ReadWriteDAOImpl<Item, Long> implements MainPageItemsDao {

    /**
     * Этот метод используется для выборки Item
     * по id категории
     */
    @Override
    public List<Item> findItemsByCategoryId(Long categoryId) {
        return entityManager.createQuery("SELECT i FROM Item i JOIN i.categories c where c.id = :id", Item.class)
                .setParameter("id", categoryId)
                .getResultList();
    }

    @Override
    public List<Item> findPopularItems() {
        return entityManager.createQuery("SELECT i FROM Item i WHERE i.isModerateAccept = true AND i.isModerated = true ORDER BY i.count DESC", Item.class)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List<Item> findItems() {
        return entityManager.createQuery("SELECT i FROM Item i WHERE i.isModerateAccept = true AND i.isModerated = true", Item.class)
                .getResultList();
    }

    @Override
    public Long findCountItemsByCategoryId(Long id) {
        return getSingleResultOrNull(entityManager.createQuery("SELECT COUNT (i.id) FROM Item i JOIN i.categories c where c.id = :id", Long.class)
                .setParameter("id", id))
                .orElse(0L);
    }

    @Override
    public List<Item> findItemsByCategoryIdWithPagination(Long id, int pageNum, int pageSize) {
        return entityManager.createQuery("SELECT i FROM Item i JOIN i.categories c where c.id = :id", Item.class)
                .setParameter("id", id)
                .setFirstResult((pageNum-1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
