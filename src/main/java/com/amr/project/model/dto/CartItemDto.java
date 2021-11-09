package com.amr.project.model.dto;

import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Shop;
import com.amr.project.model.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class CartItemDto {

    private Long id;
    private ItemDto item;
    private ShopDto shop;
    private UserDto user;
    private String anonID;
    private int quantity;
    private BigDecimal getSubTotal;
}
