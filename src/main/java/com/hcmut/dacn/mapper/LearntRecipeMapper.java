package com.hcmut.dacn.mapper;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.dto.LearntRecipeDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LearntRecipeMapper {
    @Mapping(target = "recipe.numEvaluation",ignore = true)
    @Mapping(target = "recipe.scheduledDate",ignore = true)
    @Mapping(target = "recipe.learntDate",source = "evaluation.createdDate")
    @Mapping(target = "images",ignore = true)
    LearntRecipeDto toDTO(LearntRecipeEntity learntRecipe);
    List<LearntRecipeDto> toDTOs(List<LearntRecipeEntity> learntRecipes);
}
