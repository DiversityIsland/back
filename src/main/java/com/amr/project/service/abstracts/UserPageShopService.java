package com.amr.project.service.abstracts;

import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Shop;

import java.util.List;

public interface UserPageShopService extends ReadWriteService<Shop, Long> {
    List<ShopDto> getShopsByUserId(Long userId);
    void makeShopPretendentToBeDeletedById(Long id);
}
