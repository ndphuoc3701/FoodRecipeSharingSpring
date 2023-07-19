package com.hcmut.dacn.request;

import lombok.Data;

import java.util.List;


@Data
public class RecipeRequest {
    private String name;

    private byte[] image;

    private List<MaterialRecipeRequest> materialRecipeRequests;

    private List<InstructionRequest> instructionRequests;

    private Long ownerId;
}
