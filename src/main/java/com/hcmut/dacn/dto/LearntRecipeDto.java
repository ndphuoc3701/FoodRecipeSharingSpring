package com.hcmut.dacn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearntRecipeDto {
    private Long id;
    private RecipeDto recipe;
    private EvaluationDto evaluation;
}
