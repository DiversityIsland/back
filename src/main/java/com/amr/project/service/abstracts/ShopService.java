package com.amr.project.service.abstracts;


import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Shop;
import com.amr.project.model.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ShopService extends ReadWriteService<Shop, Long> {

    Shop findShopByName(String name);

    List<Shop> getUnmoderatedShops();

    List<Shop> findModeratedShops();

    Shop addShop(ShopDto shopDto, User user);
}
