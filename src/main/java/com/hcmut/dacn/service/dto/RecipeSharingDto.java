package com.hcmut.dacn.service.dto;

import com.hcmut.dacn.entity.IngredientRecipeEntity;
import com.hcmut.dacn.entity.InstructionEntity;
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
