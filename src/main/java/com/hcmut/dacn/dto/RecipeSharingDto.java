package com.hcmut.dacn.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeSharingDto {
    private String name;
    private String image;
    private List<IngredientRecipeDto> ingredients;
    private List<InstructionDto> instructions;
    private Long ownerId;
}
