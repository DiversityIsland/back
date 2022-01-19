package com.amr.project.webapp.rest_controller.moderator_rest;

import com.amr.project.converter.ItemMapper;
import com.amr.project.model.dto.ItemDto;
import com.amr.project.service.abstracts.ItemService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.http.Header;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author denisqaa on 06.08.2021.
 * @project platform
 */

@CrossOrigin(origins = "http://localhost")
@Api(value = "API для модератора", description = "Предоставляет набор методов для одобрения товаров")
@RequestMapping("/moderator/api/items")
@RestController
public class ModeratorItemsRestController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    public ModeratorItemsRestController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @Operation(
            summary = "Получение немодерированных предметов",
            description = "Возвращает все предметы у которых флаг isModerated = false"
    )
    @GetMapping("/getUnmoderatedItems")
    public ResponseEntity<List<ItemDto>> getUnmoderatedItems() {
        return new ResponseEntity<>(
                itemService.getUnmoderatedItems()
                        .stream()
                        .map(itemMapper::itemToItemDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/getOneUnmoderatedItem/{id}")
    public ResponseEntity<ItemDto> getOneUnmoderatedItem(@PathVariable("id") Long id) {
        return itemService.findItemById(id).isModerated() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(itemMapper.itemToItemDto(itemService.findItemById(id)), HttpStatus.OK);
    }

    @PutMapping("/editItem")
    public ResponseEntity<ItemDto> editItem(@RequestBody ItemDto itemDto) {
        itemService.update(itemMapper.itemDtoToItem(itemDto));
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.findItemById(itemDto.getId())), HttpStatus.OK);
    }

    @GetMapping("/getUnmoderatedItemsCount")
    public ResponseEntity<Long> getUnmoderatedItemsCount() {
        return new ResponseEntity<>((long) itemService
                .getUnmoderatedItems()
                .size(), HttpStatus.OK);
    }

}
