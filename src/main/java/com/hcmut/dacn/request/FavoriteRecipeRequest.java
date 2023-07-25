package com.hcmut.dacn.request;

import lombok.Data;


@Data
public class FavoriteRecipeRequest {
    private Long userId;
    private Long recipeId;
}
