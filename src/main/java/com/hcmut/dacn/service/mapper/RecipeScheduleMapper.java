package com.hcmut.dacn.service.mapper;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeScheduleMapper {
    @Mapping(target = "userId", source = "recipeSchedule.user.id")
    @Mapping(target = "recipe.material",ignore = true)
    @Mapping(target = "recipe.instruction",ignore = true)
    @Mapping(target = "recipe.owner",ignore = true)
    @Mapping(target = "recipe.numStar",ignore = true)
    @Mapping(target = "recipe.numFavorite",ignore = true)
    @Mapping(target = "recipe.numEvaluation",ignore = true)
    RecipeScheduleDto toDto(RecipeScheduleEntity recipeSchedule);
    List<RecipeScheduleDto> toDtos(List<RecipeScheduleEntity> recipeSchedules);
}
