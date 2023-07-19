package com.hcmut.dacn.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRecipeDto {
    Long id;
    RecipeDto recipe;
    String note;
}
