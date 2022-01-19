package com.amr.project.converter;

import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Shop;
import com.amr.project.model.entity.User;
import com.github.scribejava.core.base64.Base64;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

/**
 * @author denisqaa on 29.07.2021.
 * @project platform
 */


@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        uses = {ReviewMapper.class, ImageMapper.class},
        componentModel = "spring")
public interface ShopMapper {

    ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "logo.url", target = "logo"),
            @Mapping(source = "location.name", target = "location"),
            @Mapping(source = "logo.picture", target = "logoarray"),
            @Mapping(source = "moderated", target = "moderated"),
            @Mapping(source = "moderateAccept", target = "moderateAccept"),
            @Mapping(source = "moderatedRejectReason", target = "moderatedRejectReason")
    })
    ShopDto shopToShopDto(Shop shop);

    @Mappings({
            @Mapping(source = "username", target = "user.username"),
            @Mapping(source = "location", target = "location.name"),
            @Mapping(source = "logo", target = "logo.url"),
            @Mapping(source = "logoarray", target = "logo.picture"),
            @Mapping(source = "moderated", target = "isModerated"),
            @Mapping(source = "moderateAccept", target = "isModerateAccept"),
            @Mapping(source = "moderatedRejectReason", target = "moderatedRejectReason")
    })
    Shop shopDtoToShop(ShopDto shopDto);

    default String map(byte[] picture) {
        String str_ = "data:jpg;base64,";
        str_ += Base64.encode(picture);
        return str_;
    }

    default byte[] map(String logoarray) {
        if (logoarray != null) {
            String[] stringBytesArray = logoarray.split(",");
            byte[] picture = java.util.Base64.getEncoder().encode(stringBytesArray[1].trim().getBytes());
            return picture;
        } else return null;
    }

    default Long map(Shop shop) {
        return shop.getId();
    }

    default String map(User user) {
        return user != null ? user.getUsername() : "No user!";
    }


    List<ShopDto> toShopDto(List<Shop> shop);
}
