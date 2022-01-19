package com.amr.project.webapp.rest_controller.moderator_rest;

import com.amr.project.converter.ReviewMapper;
import com.amr.project.model.dto.ReviewDto;
import com.amr.project.model.entity.Review;
import com.amr.project.service.abstracts.ItemService;
import com.amr.project.service.abstracts.ReviewService;
import com.amr.project.service.abstracts.ShopService;
import com.amr.project.service.abstracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author denisqaa on 09.08.2021.
 * @project platform
 */

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/moderator/api/reviews")
public class ModeratorReviewRestController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final ShopService shopService;
    private final ItemService itemService;
    private final UserService userService;

    public ModeratorReviewRestController(ReviewService reviewService, ReviewMapper reviewMapper, ShopService shopService, ItemService itemService, UserService userService) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
        this.shopService = shopService;
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/getUnmoderatedReviews")
    public ResponseEntity<List<ReviewDto>> getUnmoderatedReviews() {
        return new ResponseEntity<>(
                reviewService
                        .getUnmoderatedReviews()
                        .stream()
                        .map(reviewMapper::reviewToReviewDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/getOneUnmoderatedReview/{id}")
    public ResponseEntity<ReviewDto> getOneUnmoderatedItem(@PathVariable("id") Long id) {
        return reviewService.getByKey(id).isModerated() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(reviewMapper.reviewToReviewDto(reviewService.getByKey(id)), HttpStatus.OK);
    }

//    @CrossOrigin
    @PutMapping("/editReview")
    public ResponseEntity<ReviewDto> editReview(@RequestBody ReviewDto reviewDto) {
        Review review = reviewMapper.reviewDtoToReview(reviewDto);
        review.setUser(userService.getByKey(reviewDto.getUserId()));
        review.setShop(shopService.getByKey(reviewDto.getShopId()));
        review.setItem(itemService.getByKey(reviewDto.getItemId()));
        reviewService.update(review);
        return new ResponseEntity<>(reviewMapper.reviewToReviewDto(reviewService.getByKey(reviewDto.getId())), HttpStatus.OK);
    }

    @GetMapping("/getUnmoderatedReviewsCount")
    public ResponseEntity<Long> getUnmoderatedReviewsCount() {
        return new ResponseEntity<>((long) reviewService
                .getUnmoderatedReviews()
                .size(), HttpStatus.OK);
    }

}
