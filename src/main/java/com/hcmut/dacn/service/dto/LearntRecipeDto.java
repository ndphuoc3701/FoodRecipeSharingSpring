package com.hcmut.dacn.service.dto;

import com.hcmut.dacn.entity.ImageLearnEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearntRecipeDto {
    RecipeDto recipe;
    String note;
    List<String> imageLearns;
}
