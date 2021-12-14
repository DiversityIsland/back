package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.ItemMapper;
import com.amr.project.converter.ShopMapper;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Shop;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author denisqaa on 28.07.2021.
 * @project platform
 */
@RestController
@RequestMapping("/seller/api")
public class SellerRestController {

    private final ShopService shopService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final ShopMapper shopMapper;

    /**
     * Ругается что не находит бинов сейчас, они появляются после билда
     * игнорируйте эту ошибук
     **/

    public SellerRestController(ShopService shopService, ItemService itemService, ItemMapper itemMapper, ShopMapper shopMapper) {
        this.shopService = shopService;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.shopMapper = shopMapper;
    }

    private static boolean isNumeric(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    // TODO: 02.08.2021 ПЕРЕПИСАТЬ ПОД RESPONSEENTRITY VOID, PATCH ВОЗВРАЩАЕТ ОБНОВЛЕННЫЙ ИТЕМ, С ФРОНТА ПРИХОДИТ ДТО -- DELIVERED
    @GetMapping("/{shopIdOrName}/settings")
    public ResponseEntity<ShopDto> getShop(@PathVariable("shopIdOrName") String shopIdOrName) {
        if (isNumeric(shopIdOrName)) {
            return new ResponseEntity<>(shopMapper.shopToShopDto(shopService.getByKey(Long.parseLong(shopIdOrName))), HttpStatus.OK);
        }
        return new ResponseEntity<>(shopMapper.shopToShopDto(shopService.findShopByName(shopIdOrName)), HttpStatus.OK);
    }

    @PutMapping(value = "/{shopIdOrName}/settings")
    public ResponseEntity<ShopDto> updateShop(@RequestBody ShopDto shopDto) {
        shopService.update(shopMapper.shopDtoToShop(shopDto));
        return new ResponseEntity<>(shopMapper.shopToShopDto(shopService.findShopByName(shopDto.getName())), HttpStatus.OK);
    }

    @PostMapping(value = "/{shopIdOrName}/product/{productIdOrName}/newProduct")
    public ResponseEntity<Void> addProduct(@RequestBody ItemDto itemDto,
                                           @PathVariable String productIdOrName, @PathVariable String shopIdOrName) {
        Shop shop = isNumeric(shopIdOrName) ? shopService.getByKey(Long.parseLong(shopIdOrName)) : shopService.getByName(shopIdOrName);
        List<Item> items = shop.getItems();
        itemService.update(itemMapper.itemDtoToItem(itemDto));
        Item item = itemService.getByName(itemDto.getName());
        item.setShop(shop);
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        shop.setItems(items);
        shopService.update(shop);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{shopIdOrName}/product/{productIdOrName}/edit")
    public ResponseEntity<ItemDto> getProduct(@PathVariable String productIdOrName, @PathVariable String shopIdOrName) {
        Shop shop = isNumeric(shopIdOrName) ? shopService.getByKey(Long.parseLong(shopIdOrName)) : shopService.findShopByName(shopIdOrName);
        Item item = isNumeric(productIdOrName) ? itemService.findItemById(Long.parseLong(productIdOrName)) : itemService.findItemByName(productIdOrName);
        boolean isShopItem = shop.getItems().contains(item);
        return isShopItem ? new ResponseEntity<>(itemMapper.itemToItemDto(item), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping(value = "/{shopIdOrName}/product/{productIdOrName}/edit")
    public ResponseEntity<ItemDto> updateProduct(@RequestBody ItemDto itemDto,
                                                 @PathVariable String productIdOrName,
                                                 @PathVariable String shopIdOrName) {
        Shop shop = isNumeric(shopIdOrName) ? shopService.getByKey(Long.parseLong(shopIdOrName)) : shopService.getByName(shopIdOrName);
        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setShop(shop);
        itemService.update(item);
        shopService.update(shop);
        return new ResponseEntity<>(itemMapper.itemToItemDto
                (itemService.findItemByName(item.getName())), HttpStatus.OK);
    }


    @DeleteMapping(value = "/{shopIdOrName}/product/{productIdOrName}/edit")
    public ResponseEntity<Void> deleteProduct(@PathVariable String shopIdOrName, @PathVariable String productIdOrName) {
        Shop shop = isNumeric(shopIdOrName) ? shopService.getByKey(Long.parseLong(shopIdOrName)) : shopService.findShopByName(shopIdOrName);
        Item item = isNumeric(productIdOrName) ? itemService.findItemById(Long.parseLong(productIdOrName)) : itemService.findItemByName(productIdOrName);
        boolean isShopItem = shop.getItems().contains(item);
        if (isShopItem) {
            item.setPretendentToBeDeleted(true);
            shopService.persist(shop);
        }
        return isShopItem ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
