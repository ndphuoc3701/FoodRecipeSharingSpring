package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.RecipeEntity;

import java.util.List;
public interface RecipeDao {
    List<RecipeEntity> getAll();
    RecipeEntity getByRecipeId(Long recipeId);
    List<RecipeEntity> getAllByOwnerId(Long ownerId);
    RecipeEntity create(RecipeEntity recipe);
}
