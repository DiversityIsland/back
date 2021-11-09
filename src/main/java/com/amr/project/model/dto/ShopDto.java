package com.amr.project.model.dto;

import com.amr.project.model.entity.Discount;
import com.amr.project.model.entity.Image;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Review;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@ApiModel
@Data
@ToString
public class ShopDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String description;
    private String logo;
    private String location;
    private List<Item> items;
    private List<Review> reviews;
    private double rating;
    private Collection<Discount> discounts;
    private int count;
    private String username;
    private boolean isModerated;
    private boolean isModerateAccept;
    private String moderatedRejectReason;
    private boolean isPretendentToBeDeleted;
    private String logoarray; // Должна быть картинка в массиве byte
}