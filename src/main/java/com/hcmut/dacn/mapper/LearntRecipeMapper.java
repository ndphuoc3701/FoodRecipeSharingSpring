package com.hcmut.dacn.mapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.dto.LearntRecipeDto;
import org.mapstruct.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public interface LearntRecipeMapper {
//    @Mapping(target = "recipe.numEvaluation",ignore = true)
//    @Mapping(target = "recipe.scheduledDate",ignore = true)
//    @Mapping(target = "recipe.learntDate",source = "evaluation.createdDate")
//    @Mapping(target = "recipe.learntDate",expression = "java(getDate(learntRecipe))")
    @Mapping(target = "recipe",source = "learntRecipe.recipe")
    @Mapping(target = "recipe.image",expression = "java(new String(recipeEntity.getImageData(), StandardCharsets.UTF_8))")
    @Mapping(target = "evaluation.images", expression = "java(toImageDtos(evaluationEntity.getImages()))")
    LearntRecipeDto toDTO(LearntRecipeEntity learntRecipe);
    List<LearntRecipeDto> toDTOs(List<LearntRecipeEntity> learntRecipes);

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    default Date getDate(LearntRecipeEntity learntRecipe){
        return learntRecipe.getEvaluation().getCreatedDate();
    }

    default List<ImageDto> toImageDtos(List<ImageEntity> imageEntities){
        return imageEntities.stream().map(imageEntity -> {
            ImageDto imageDto= new ImageDto();
            imageDto.setData(new String(imageEntity.getData(), StandardCharsets.UTF_8));
            return imageDto;
        }).collect(Collectors.toList());
    }
}
