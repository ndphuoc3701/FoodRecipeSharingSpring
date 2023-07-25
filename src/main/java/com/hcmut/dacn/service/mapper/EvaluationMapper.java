package com.hcmut.dacn.service.mapper;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public interface EvaluationMapper {
    @Named(value = "recipe")
    @Mapping(target = "image",expression = "java(new String(evaluation.getRecipe().getImageData(),StandardCharsets.UTF_8))")
    EvaluationRecipeDto toRecipeDto(EvaluationEntity evaluation);
    @IterableMapping(qualifiedByName = "recipe")
    List<EvaluationRecipeDto> toRecipeDtos(List<EvaluationEntity> evaluations);

    @Named(value = "user")
    @Mapping(target = "evaluationLearntRecipeDto",source= "evaluation.recipe")
    @Mapping(target = "evaluationLearntRecipeDto.image",expression = "java(new String(evaluation.getRecipe().getImageData(),StandardCharsets.UTF_8))")
    EvaluationUserDto toUserDto(EvaluationEntity evaluation);
    @IterableMapping(qualifiedByName = "user")
    List<EvaluationUserDto> toUserDtos(List<EvaluationEntity> evaluations);
}
