package com.amr.project.service.impl;

import com.amr.project.converter.ItemMapper;
import com.amr.project.dao.abstracts.ItemDao;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.entity.Item;
import com.amr.project.service.abstracts.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, ItemMapper itemMapper) {
        super(itemDao);
        this.itemDao = itemDao;
        this.itemMapper = itemMapper;
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
    public void SetStartItemsByShopId(Long id) {
        itemDao.setStartItemsByShopId(id);
    }

    @Override
    public void SetCompleteItemsByShopId(Long id) {
        itemDao.setCompleteItemsByShopId(id);
    }

    @Override
    public List<Item> getSoldItemsByShopId(Long id) {
        return itemDao.getSoldItemsByShopId(id);
    }

    @Override
    public void SetSentItemsByShopId(Long id) {
         itemDao.setSentItemsByShopId(id);
    }

    @Override
    public void SetDeliveredItemsByShopId(Long id) {
         itemDao.setDeliveredItemsByShopId(id);
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

    @Override
    public List<ItemDto> findByName(String name) {
        String[] words = name.toLowerCase().split("[\\p{Punct}\\s]+");
        List<ItemDto> itemsDto = itemMapper.toItemsDto(
                itemDao.searcheByWords(words)
        );
        itemsDto.sort( (a, b) -> {
            String nameA = a.getName().toLowerCase();
            String nameB = b.getName().toLowerCase();
            int countA = 0;
            int countB = 0;
            for(String word : words) {
                if(nameA.contains(word)) {
                    countA++;
                }
                if(nameB.contains(word)) {
                    countB++;
                }
            }
            return countB - countA;
        });
        return itemsDto;
    }

}

