package com.amr.project.service.impl;

import com.amr.project.converter.UserMapper;
import com.amr.project.dao.abstracts.AddressDao;
import com.amr.project.dao.abstracts.CityDao;
import com.amr.project.dao.abstracts.CountryDao;
import com.amr.project.dao.abstracts.UserDao;
import com.amr.project.model.dto.AddressDto;
import com.amr.project.model.dto.UserDto;
import com.amr.project.model.entity.*;
import com.amr.project.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends ReadWriteServiceImpl<User, Long> implements UserService {

    private final UserDao userDao;
    private AddressDao addressDao;
    private CountryDao countryDao;
    private CityDao cityDao;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           AddressDao addressDao,
                           CountryDao countryDao,
                           CityDao cityDao, UserMapper userMapper) {
        super(userDao);
        this.userDao = userDao;
        this.addressDao = addressDao;
        this.countryDao = countryDao;
        this.cityDao = cityDao;
        this.userMapper = userMapper;
    }

    public Optional<User> findByUsername(String username)  {
        return userDao.findByUsername(username);
    };

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void updateUserOnUserPage(User user, UserDto userDto) {

        User oldUser = userMapper.dtoToUser(userDto);

        Image image = Image.builder()
                .url(oldUser.getImages().getUrl())
                .picture(oldUser.getImages().getPicture())
                .isMain(true)
                .build();

        user.setImages(image);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAge(userDto.getAge());
        user.setBirthday(userDto.getBirthday());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setGender(userDto.getGender());

        Address address =  user.getAddress();
        City city = address.getCity();
        Country country = address.getCountry();
        address.setStreet(userDto.getAddress().getStreet());
        address.setHouse(userDto.getAddress().getHouse());
        address.setCityIndex(userDto.getAddress().getCityIndex());
        city.setName(userDto.getAddress().getCity());
        country.setName(userDto.getAddress().getCountry());
        cityDao.update(city);
        countryDao.update(country);
        addressDao.update(address);
        super.update(user);

    }

    @Override
    public Long getLastPageNumBySearchName(String name, int pageSize) {
        Long countTotalEl =  userDao.findCountTotalElementBySearchName(name);
        if (countTotalEl == 0) return 1L;
        if (countTotalEl % pageSize == 0) {
            return countTotalEl / pageSize;
        }
        return  (countTotalEl / pageSize) + 1;
    }

    @Override
    public List<User> findUserBySearchNameWithPagination(String name, int pageNum, int pageSize) {
        return userDao.findUserBySearchNameWithPagination(name, pageNum, pageSize);
    }

    @Override
    public List<User> findConnectUsersById(Long currentId) {
        return userDao.findConnectUsersById(currentId);
    }
}
