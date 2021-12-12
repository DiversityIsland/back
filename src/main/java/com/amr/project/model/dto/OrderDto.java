package com.amr.project.model.dto;

import com.amr.project.model.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

    private Long id;

    private String buyerName;

    private String buyerPhone;

    private Collection<Item> items;

    private String date;

    private String status;

    private String index;

    private String country;

    private String city;

    private String street;

    private String house;

    private BigDecimal total;

    private String paymentMethod;

    private String intent;

    private String description;

    private String paymentCurrency;

    private String paymentStatus;

}

