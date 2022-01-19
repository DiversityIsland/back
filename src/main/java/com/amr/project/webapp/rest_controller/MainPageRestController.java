package com.amr.project.webapp.rest_controller;

import com.amr.project.model.dto.ItemDto;
import com.amr.project.service.abstracts.MainPageItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mainpage")
public class MainPageRestController {

    private final MainPageItemService mainPageItemService;


    public MainPageRestController(MainPageItemService mainPageItemService) {
        this.mainPageItemService = mainPageItemService;
    }

    @GetMapping("/item/category/{id}")
    public ResponseEntity<List<ItemDto>> getItemDtoByCategoryId(@PathVariable("id") Long id) {
        List<ItemDto> itemsDto = mainPageItemService.findItemsByCategoryId(id);
        return new ResponseEntity<>(itemsDto, HttpStatus.OK);
    }

    @GetMapping("/item/lastpage/category/{id}/{pageSize}")
    public ResponseEntity<Long> getLastPageNumItemsByCategoryId(@PathVariable Long id,
                                                                @PathVariable Integer pageSize) {
        long lastPage = mainPageItemService.findLastPageItemsByCategoryId(id, pageSize);
        return new ResponseEntity<>(lastPage, HttpStatus.OK);
    }

    @GetMapping("/item/category/{id}/{pagenum}/{pageSize}")
    public ResponseEntity<List<ItemDto>> getItemsByCategoryIdWithPagination(@PathVariable Long id,
                                                                            @PathVariable Integer pagenum,
                                                                            @PathVariable Integer pageSize) {
        List<ItemDto> itemsDto = mainPageItemService.findItemsByCategoryIdWithPagination(id, pagenum, pageSize);
        return new ResponseEntity<>(itemsDto, HttpStatus.OK);
    }

    @GetMapping("/search/{searchWords}")
    @ResponseBody
    public ResponseEntity<List<ItemDto>> searchItemsMainByName(@PathVariable("searchWords") String itemName) {
        return new ResponseEntity<>(mainPageItemService.searchItemsMainByName(itemName),
                HttpStatus.OK);
    }

}
