package com.hcmut.dacn.mapper;

import com.hcmut.dacn.dto.ScheduleRecipeDto;
import com.hcmut.dacn.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public interface ScheduleRecipeMapper {
//    @Mapping(target = "recipe",source = "learntRecipe.recipe")
    @Mapping(target = "recipe.image",expression = "java(new String(recipeEntity.getImageData(), StandardCharsets.UTF_8))")
    ScheduleRecipeDto toDto(ScheduleRecipeEntity recipeSchedule);
    List<ScheduleRecipeDto> toDtos(List<ScheduleRecipeEntity> recipeSchedules);
}
