package com.amr.project.model.dto;

import com.amr.project.model.entity.Favorite;
import com.amr.project.model.entity.Item;
import com.amr.project.model.entity.Shop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class FavoriteDto {

    public FavoriteDto (Favorite fav) {
        this.items = fav.getItems();
        this.shops = fav.getShops();
    }

    private Collection<Shop> shops;

    private Collection<Item> items;
}
