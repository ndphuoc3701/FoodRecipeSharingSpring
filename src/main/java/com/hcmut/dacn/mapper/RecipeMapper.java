package com.hcmut.dacn.mapper;

import com.hcmut.dacn.dto.*;
import com.hcmut.dacn.entity.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.lang.String;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",imports = {StandardCharsets.class, Collectors.class})
public abstract class RecipeMapper {
    @Named(value = "useMe")
    @Mapping(target = "image",expression = "java(new String(recipe.getImageData(),StandardCharsets.UTF_8))")
    public abstract RecipeDto toDto(RecipeEntity recipe);

    @Mapping(target = "ingredients",expression = "java(recipe.getIngredientRecipes().stream().map(IngredientRecipeEntity::getName).collect(Collectors.toList()))")
    @Mapping(target = "image",expression = "java(new String(recipe.getImageData(),StandardCharsets.UTF_8))")
    public abstract RecipeDto toEsDto(RecipeEntity recipe);

    @IterableMapping(qualifiedByName = "useMe")
    public abstract List<RecipeDto> toDtos(List<RecipeEntity> recipes);

    @Mapping(target = "recipe",source = "recipe")
    @Mapping(target = "recipeSharing.ingredients", source = "recipe.ingredientRecipes")
    @Mapping(target = "recipeSharing.instructions", expression= "java(toInstructionDtos(recipeEntity.getInstructions()))")
    @Mapping(target = "user",source = "recipe.owner")
    public abstract RecipeDetailDto toRecipeDetailDto(RecipeEntity recipe);

    List<InstructionDto>toInstructionDtos(List<InstructionEntity> instructions){

        return instructions.stream().map(r->{
            InstructionDto instructionDto=new InstructionDto();
            instructionDto.setImages(r.getImages().stream().map(i->{
                ImageDto imageDto= new ImageDto();
                imageDto.setData(new String(i.getData(),StandardCharsets.UTF_8));
                return imageDto;
            }).collect(Collectors.toList()));
            instructionDto.setContent(r.getContent());
            instructionDto.setStepOrder(r.getStepOrder());
            return instructionDto;
        }).collect(Collectors.toList());
    }
}
