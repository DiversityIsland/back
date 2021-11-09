package com.amr.project.service.impl;


import com.amr.project.converter.ShopMapper;
import com.amr.project.dao.abstracts.ShopDao;
import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Country;
import com.amr.project.model.entity.Image;
import com.amr.project.model.entity.Shop;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.CountryService;
import com.amr.project.service.abstracts.ShopService;
import com.amr.project.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
public class ShopServiceImpl extends ReadWriteServiceImpl<Shop, Long> implements ShopService {

    private final ShopDao shopDao;
    private final ShopMapper shopMapper;
    private final CountryService countryService;
    private final UserService userService;

    @Autowired
    public ShopServiceImpl(ShopDao shopDao, ShopMapper shopMapper, CountryService countryService,
                           UserService userService) {
        super(shopDao);
        this.shopDao = shopDao;
        this.shopMapper = shopMapper;
        this.countryService = countryService;
        this.userService = userService;
    }

    @Override
    public Shop findShopByName(String name) {
        return shopDao.findShopByName(name);
    }

    @Override
    public List<Shop> getUnmoderatedShops() {
        return shopDao.findUnmoderatedShops();
    }

    @Override
    public List<Shop> findModeratedShops() {
        return shopDao.findModeratedShops();
    }

    @Transactional
    public Shop addShop (ShopDto shopDto, User user) {
        Shop shop = shopMapper.shopDtoToShop(shopDto);
        Image image;
        if (shop.getLogo().getUrl() == null || shop.getLogo().getPicture() == null) {
            image = null;
        } else {
            image = Image.builder()
                    .url(shop.getLogo().getUrl())
                    .picture(shop.getLogo().getPicture())
                    .isMain(true)
                    .build();
        }

        if (countryService.getByName(shopDto.getLocation()) == null) {
            countryService.persist(new Country(shopDto.getLocation()));
        }
        shop.setLocation(countryService.getByName(shopDto.getLocation()));

        if (userService.findByUsername(user.getUsername()).isEmpty()) {
            userService.persist(user);
        }
        shop.setUser(user);
        shop.setLogo(image);

        shopDao.persist(shop);

        return shop;
    }
}
