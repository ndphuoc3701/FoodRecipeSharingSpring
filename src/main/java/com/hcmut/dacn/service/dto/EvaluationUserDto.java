package com.hcmut.dacn.service.dto;

import com.hcmut.dacn.service.EvaluationLearntRecipeDto;
import lombok.Data;

@Data
public class EvaluationUserDto {
    Long id;
    private String content;
    private Double numStar;
    private EvaluationLearntRecipeDto evaluationLearntRecipeDto;

}
