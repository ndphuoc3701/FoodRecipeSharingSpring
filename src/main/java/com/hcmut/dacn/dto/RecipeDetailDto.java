package com.hcmut.dacn.dto;

import lombok.Data;

@Data
public class RecipeDetailDto {
    RecipeDto recipe;
    UserDto user;
    RecipeSharingDto recipeSharing;
}
