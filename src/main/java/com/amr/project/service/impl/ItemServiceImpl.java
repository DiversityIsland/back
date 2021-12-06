package com.amr.project.service.impl;

import com.amr.project.dao.abstracts.ItemDao;
import com.amr.project.model.entity.Item;
import com.amr.project.service.abstracts.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author denisqaa on 28.07.2021.
 * @project platform
 */

@Service
@Transactional
public class ItemServiceImpl extends ReadWriteServiceImpl<Item, Long>
        implements ItemService {
    private final ItemDao itemDao;

    public ItemServiceImpl(ItemDao itemDao) {
        super(itemDao);
        this.itemDao = itemDao;
    }

    @Override
    public Item findItemById(Long id) {
        return itemDao.findItemById(id);
    }

    @Override
    public Item findItemByName(String name) {
        return itemDao.findItemByName(name);
    }

    @Override
    public List<Item> getItemsByShopId(Long id) {
        return itemDao.getItemsByShopId(id);
    }

//МЕТОДЫ НИЖЕ ДОЛЖНЫ БУДУТ РЕАЛИЗОВЫВАТЬ ПРОВЕРКУ НА СТАТУСНОСТЬ,ВОЗМОЖН НУЖНО БУДЕТ ТОЛЬК ДЛЯ ВСЕХ,КРОМЕ PAID
    @Override
    public List<Item> SetRegistredItemsByShopId(Long id) {
        return itemDao.setRegistredItemsByShopId(id);
    }

    @Override
    public List<Item> SetPaidItemsByShopId(Long id) {
        return itemDao.setPaidItemsByShopId(id);
    }

    @Override
    public List<Item> getSoldItemsByShopId(Long id) {
        return itemDao.getSoldItemsByShopId(id);
    }

    @Override
    public List<Item> SetSentItemsByShopId(Long id) {
        return itemDao.setSentItemsByShopId(id);
    }

    @Override
    public List<Item> SetDoneItemsByShopId(Long id) {
        return itemDao.setDoneItemsByShopId(id);
    }

    //

    @Override
    public List<Item> getUnmoderatedItems() {
        return itemDao.getUnmoderatedItems();
    }

    @Override
    public List<Item> findModeratedShops() {
        return itemDao.getModeratedItems();
    }

    @Override
    public void makeItemPretendentToBeDeletedById(Long id) {
        Item item = super.getByKey(id);
        item.setPretendentToBeDeleted(true);
        super.update(item);
    }
}

