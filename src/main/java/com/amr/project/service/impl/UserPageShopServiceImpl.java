package com.amr.project.service.impl;

import com.amr.project.converter.ShopMapper;
import com.amr.project.dao.abstracts.UserPageShopDao;
import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Shop;
import com.amr.project.service.abstracts.UserPageShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserPageShopServiceImpl extends ReadWriteServiceImpl<Shop,Long> implements UserPageShopService {

    private UserPageShopDao userPageShopDao;
    private ShopMapper shopMapper;

    @Autowired
    public UserPageShopServiceImpl(UserPageShopDao userPageShopDao,
                                   ShopMapper shopMapper) {
        super(userPageShopDao);
        this.userPageShopDao = userPageShopDao;
        this.shopMapper = shopMapper;
    }
    @Override
    public List<ShopDto> getShopsByUserId(Long userId) {
        return shopMapper.toShopDto(userPageShopDao.getShopsByUserId(userId));
    }

    @Override
    public void makeShopPretendentToBeDeletedById(Long id) {
        Shop shop = super.getByKey(id);
        shop.setPretendentToBeDeleted(true);
        super.update(shop);
    }
}
