package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.ItemMapper;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.entity.Image;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Review;
import com.amr.project.service.abstracts.ImageService;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.MainPageItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/item")
public class ItemRestController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final ImageService imageService;
    private final MainPageItemService mainPageItemService;

    @Autowired
    public ItemRestController(ItemService itemService, ItemMapper itemMapper, ImageService imageService,
                              MainPageItemService mainPageItemService) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.imageService = imageService;
        this.mainPageItemService = mainPageItemService;
    }

    /*
    * json список популярных товаров нашего магазина
    * */
    @GetMapping("/popular")
    public ResponseEntity<List<ItemDto>> getItem() {
        return new ResponseEntity<>(mainPageItemService.findPopularItems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.getByKey(id)), HttpStatus.OK);
    }

    @PutMapping("/save")
    public ResponseEntity<Item> saveItem(@RequestBody ItemDto itemDto) {
        Set<Image> images = new HashSet<>();

        for (int i = 0; i < itemDto.getImages().length; i++) {
                Image image = imageService.getByUrl(itemDto.getImages()[i]);
                if (image.getUrl() != null) {
                    images.add(image);
                } else {
                    images.add(Image.builder()
                            .url(itemDto.getImages()[i])
                            .picture(new byte[0])
                            .isMain(i == 0)
                            .build());
                }
        }

        Item item = itemMapper.itemDtoToItem(itemDto);
        Collection<Review> reviews = itemService.getByKey(itemDto.getId()).getReviews();
        if (!reviews.isEmpty()) {
            item.setReviews(itemService.getByKey(itemDto.getId()).getReviews());
        }

        item.setImages(images);
        itemService.update(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody  ItemDto itemDto) {

        Set<Image> images = new HashSet<>();

        for (int i = 0; i < itemDto.getImages().length; i++) {
            images.add(Image.builder()
                    .url(itemDto.getImages()[i])
                    .picture(itemDto.getImagesArray()[i].getBytes())
                    .isMain(i==0)
                    .build());
        }

        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setImages(images);
        itemService.persist(item);

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        itemService.deleteByKeyCascadeIgnore(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}