package com.hcmut.dacn.service.mapper;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeDto toDto(RecipeEntity recipe);
    List<RecipeDto> toDtos(List<RecipeEntity> recipes);
}
