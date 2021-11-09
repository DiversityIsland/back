package com.amr.project.service.abstracts;

import com.amr.project.model.entity.Item;

import java.util.List;

/**
 * @author denisqaa on 28.07.2021.
 * @project platform
 */
public interface ItemService extends ReadWriteService<Item, Long> {
    Item findItemById(Long id);

    Item findItemByName(String name);

    List<Item> getItemsByShopId(Long id);

    List<Item> getSoldItemsByShopId(Long id);

    List<Item> getUnmoderatedItems();

    List<Item> findModeratedShops();

    void makeItemPretendentToBeDeletedById(Long id);
}
