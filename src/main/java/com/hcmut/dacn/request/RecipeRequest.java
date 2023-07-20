package com.hcmut.dacn.request;

import lombok.Data;

import java.util.List;


@Data
public class RecipeRequest {
    private String name;

    private String encodedImage;

    private List<IngredientRecipeRequest> ingredientRecipeRequests;

    private List<InstructionRequest> instructionRequests;

    private Long ownerId;
}
