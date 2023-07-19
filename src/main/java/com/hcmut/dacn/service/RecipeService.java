package com.hcmut.dacn.service;

import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.request.RecipeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dao.*;
import com.hcmut.dacn.service.dto.*;
import com.hcmut.dacn.service.mapper.*;

import java.util.List;

@Service
public class RecipeService {

//    @Autowired
    private RecipeDao recipeDao;

//    @Autowired
    private UserDao userDao;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;
    public List<RecipeDto> getAll(){
        return recipeMapper.toDtos(recipeDao.getAll());
    }
    public List<RecipeDto> getByOwnerId(Long ownerId){
        return recipeMapper.toDtos(recipeDao.getAllByOwnerId(ownerId));
    }
    public RecipeDto getByRecipeId(Long recipeId){
        return recipeMapper.toDto(recipeDao.getByRecipeId(recipeId));
    }
    public RecipeDto create(RecipeRequest recipeRequest){

        RecipeEntity recipe=new RecipeEntity();
        recipe.setName(recipe.getName());
        recipe.setImageUrl(recipeRequest.getImageUrl());
        recipe.setMaterialRecipes(recipe.getMaterialRecipes());
        recipe.setInstructions(recipe.getInstructions());
        recipe.setOwner(userDao.getByUserId(recipeRequest.getOwnerId()));
//        return recipeMapper.toDto(recipeDao.create(recipe));
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }
}
