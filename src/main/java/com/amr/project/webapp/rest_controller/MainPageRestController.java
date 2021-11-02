package com.amr.project.webapp.rest_controller;

import com.amr.project.model.dto.ItemDto;
import com.amr.project.service.abstracts.MainPageItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/item/lastpage/category/{id}")
    public ResponseEntity<Long> getLastPageNumItemsByCategoryId(@PathVariable Long id) {
        int pageSize = 5;
        long lastPage = mainPageItemService.findLastPageItemsByCategoryId(id, pageSize);

        return new ResponseEntity<>(lastPage, HttpStatus.OK);
    }

    @GetMapping("/item/category/{id}/{pagenum}")
    public ResponseEntity<List<ItemDto>> getItemsByCategoryIdWithPagination(@PathVariable Long id, @PathVariable int pagenum) {
        int pageSize = 5;

        List<ItemDto> itemsDto = mainPageItemService.findItemsByCategoryIdWithPagination(id, pagenum, pageSize);

        return new ResponseEntity<>(itemsDto, HttpStatus.OK);
    }
}
