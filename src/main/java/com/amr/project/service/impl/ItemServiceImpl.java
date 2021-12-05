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

    @Override
    public List<Item> getRegistredItemsByShopId(Long id) {
        return itemDao.getRegistredItemsByShopId(id);
    }

    @Override
    public List<Item> getSoldItemsByShopId(Long id) {
        return itemDao.getSoldItemsByShopId(id);
    }

    @Override
    public List<Item> getSentItemsByShopId(Long id) {
        return itemDao.getSentItemsByShopId(id);
    }

    @Override
    public List<Item> getDoneItemsByShopId(Long id) {
        return null;
    }

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

