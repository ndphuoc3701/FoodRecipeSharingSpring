package com.hcmut.dacn.mapper;

import com.hcmut.dacn.dto.RecipeDto;
import com.hcmut.dacn.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public interface RecipeMapper {
    @Mapping(target = "image",expression = "java(new String(recipe.getImageData(),StandardCharsets.UTF_8))")
    RecipeDto toDto(RecipeEntity recipe);
    List<RecipeDto> toDtos(List<RecipeEntity> recipes);
}
