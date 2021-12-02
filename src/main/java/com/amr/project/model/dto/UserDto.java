package com.amr.project.model.dto;

import com.amr.project.model.entity.*;
import com.amr.project.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Leanz on 18.05.2021
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "email", "username", "password", "phone", "firstName", "lastName", "age", "gender"})
public class UserDto {

    private Long id;

    private String email;

    private String username;

    private String password;

    private boolean activate;

    private String activationCode;

    private String phone;

    private String firstName;

    private String lastName;

    private int age;

    private AddressDto address;

    private Collection<Role> roles;

    private Gender gender;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Calendar birthday;

    private ImageDto images;
    private String logo;
    private String logoarray;

    private Collection<Coupon> coupons;

    private Collection<DiscountDto> discounts;

    private Collection<CartItemDto> cart;

    private Collection<Order> orders;

    private Collection<Review> reviews;

    private Collection<Shop> shops;
}
