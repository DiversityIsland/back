package com.amr.project.converter;

import com.amr.project.model.dto.ItemDto;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author denisqaa on 29.07.2021.
 * @project platform
 */

@Mapper(uses = {CategoryMapper.class, ImageMapper.class,
        ReferenceCategoryMapper.class,
        ReferenceImageMapper.class, ReferenceReviewMapper.class, ReviewMapper.class},
        componentModel = "spring")
@Component
public interface ItemMapper {

    @Mappings({@Mapping(source = "shop.id", target = "shopId")})
    ItemDto itemToItemDto(Item item);

    @Mappings({@Mapping(source = "shopId", target = "shop.id")})
    Item itemDtoToItem(ItemDto itemDto);

    default List<Item> map(Shop shop) {
        return shop.getItems();
    }

    List<ItemDto> toItemsDto(List<Item> item);
    List<Item> toItems(List<ItemDto> items);
}
