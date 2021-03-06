package com.amr.project.service.impl;


import com.amr.project.converter.ShopMapper;
import com.amr.project.dao.abstracts.MainPageShopsDao;
import com.amr.project.model.dto.ShopDto;
import com.amr.project.model.entity.Shop;
import com.amr.project.service.abstracts.MainPageShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainPageShopServiceImpl extends ReadWriteServiceImpl<Shop, Long> implements MainPageShopService {
    private final MainPageShopsDao mainPageShopsDAO;
    private final ShopMapper shopMapper;

    @Autowired
    public MainPageShopServiceImpl(MainPageShopsDao mainPageShopsDAO,
                                   ShopMapper shopMapper) {
        super(mainPageShopsDAO);
        this.mainPageShopsDAO = mainPageShopsDAO;
        this.shopMapper = shopMapper;
    }


    @Override
    public List<ShopDto> findPopularShops() {
        return shopMapper.toShopDto(mainPageShopsDAO.findPopularShops());
    }
}
