package com.hcmut.dacn.mapper;


import com.hcmut.dacn.dto.admin.UserAdmin;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public interface UserMapper {

    @Mapping(target = "image",expression = "java(new String(user.getImageData(),StandardCharsets.UTF_8))")
    UserDto toDto(UserEntity user);
    List<UserDto> toDtos(List<UserEntity> users);

    UserAdmin toAdminDto(UserEntity user);
    List<UserAdmin> toAdminDtos(List<UserEntity> users);

}
