package com.amr.project.converter;

import com.amr.project.model.dto.UserDto;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.ReadWriteService;
import com.github.scribejava.core.base64.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {AddressMapper.class, RoleMapper.class, DiscountMapper.class, CategoryMapper.class,
ImageMapper.class, ReadWriteService.class, CartItemMapper.class}, componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "images.picture", target = "logoarray")
    @Mapping(source = "images.url", target = "logo")
    UserDto userToDto(User user);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "logo", target = "images.url")
    @Mapping(source = "logoarray", target = "images.picture")
    User dtoToUser(UserDto userDto);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "images.picture", target = "logoarray")
    @Mapping(source = "images.url", target = "logo")
    List<UserDto> toListUserDto(List<User> user);

    default String map(byte[] picture) {
        if (picture == null) {
            return "";
        }
        String str_ = "data:jpg;base64,";
        str_ += Base64.encode(picture);
        return str_;
    }

    default byte[] map(String logoarray) {
        if (logoarray == "") {
            return new byte[0];
        }

        if (logoarray == null) {
            return null;
        }

        String[] stringBytesArray = logoarray.split(",");
        byte[] picture = new byte[stringBytesArray.length];
        for(int i = 0; i < picture.length; i++) {
            picture[i] = Byte.parseByte(stringBytesArray[i].trim());
        }
        return picture;
    }
}