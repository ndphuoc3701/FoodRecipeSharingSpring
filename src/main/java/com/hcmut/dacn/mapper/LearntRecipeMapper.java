package com.hcmut.dacn.mapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.dto.LearntRecipeDto;
import org.mapstruct.*;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LearntRecipeMapper {
//    @Mapping(target = "recipe.numEvaluation",ignore = true)
//    @Mapping(target = "recipe.scheduledDate",ignore = true)
//    @Mapping(target = "recipe.learntDate",source = "evaluation.createdDate")
//    @Mapping(target = "recipe.learntDate",expression = "java(getDate(learntRecipe))")
    @Mapping(target = "recipe",source = "learntRecipe.recipe")
    @Mapping(target = "evaluation.images",ignore = true)
    LearntRecipeDto toDTO(LearntRecipeEntity learntRecipe);
    List<LearntRecipeDto> toDTOs(List<LearntRecipeEntity> learntRecipes);

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    default Date getDate(LearntRecipeEntity learntRecipe){
        return learntRecipe.getEvaluation().getCreatedDate();
    }
}
