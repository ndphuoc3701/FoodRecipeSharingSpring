package com.hcmut.dacn.service.mapper;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {
//    @Mapping(target = "numComment",expression = "java(evaluation.getComments().size())")
    @Mapping(target = "recipeId",source= "evaluation.recipe.id")
    EvaluationDto toDto(EvaluationEntity evaluation);
    List<EvaluationDto> toDtos(List<EvaluationEntity> evaluations);
}
