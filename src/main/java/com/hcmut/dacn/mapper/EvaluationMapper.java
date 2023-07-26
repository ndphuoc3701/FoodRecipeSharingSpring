package com.hcmut.dacn.mapper;

import com.hcmut.dacn.dto.EvaluationDto;
import com.hcmut.dacn.entity.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public interface EvaluationMapper {
    @Named(value = "recipe")
//    @Mapping(target = "user.image",expression = "java(new String(evaluation.getRecipe().getImageData(),StandardCharsets.UTF_8))")
    @Mapping(target = "user.image",expression = "java(new String(userEntity.getImageData(),StandardCharsets.UTF_8))")
    @Mapping(target = "images",ignore = true)
    EvaluationDto toDto(EvaluationEntity evaluation);
    @IterableMapping(qualifiedByName = "recipe")
    List<EvaluationDto> toDtos(List<EvaluationEntity> evaluations);

//    @Named(value = "user")
//    @Mapping(target = "evaluationLearntRecipeDto",source= "evaluation.recipe")
//    @Mapping(target = "evaluationLearntRecipeDto.image",expression = "java(new String(evaluation.getRecipe().getImageData(),StandardCharsets.UTF_8))")
//    EvaluationUserDto toUserDto(EvaluationEntity evaluation);
//    @IterableMapping(qualifiedByName = "user")
//    List<EvaluationUserDto> toUserDtos(List<EvaluationEntity> evaluations);

//    @Named(value = "user")
//    @Mapping(target = "evaluationLearntRecipeDto",source= "evaluation.recipe")
//    @Mapping(target = "evaluationLearntRecipeDto.image",expression = "java(new String(evaluation.getRecipe().getImageData(),StandardCharsets.UTF_8))")
//    @Mapping(target = "numDislike", ignore = true)
//    @Mapping(target = "numStar", ignore = true)
//    @Mapping(target = "numComment", ignore = true)
//    @Mapping(target = "user", ignore = true)
//    EvaluationRecipeDto toUserDto(EvaluationEntity evaluation);
//
//    @IterableMapping(qualifiedByName = "user")
//    List<EvaluationUserDto> toUserDtos(List<EvaluationEntity> evaluations);
}
