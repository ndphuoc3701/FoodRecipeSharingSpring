package com.hcmut.dacn.service.mapper;


import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.service.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity user);
    List<UserDto> toDtos(List<UserEntity> users);
}
