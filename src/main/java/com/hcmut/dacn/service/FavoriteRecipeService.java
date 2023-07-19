package com.hcmut.dacn.service;

import com.hcmut.dacn.entity.FavoriteRecipeEntity;
import com.hcmut.dacn.request.FavoriteRecipeRequest;
import com.hcmut.dacn.service.dao.*;
import com.hcmut.dacn.service.dto.*;

import com.hcmut.dacn.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteRecipeService {

//    @Autowired
    private FavoriteRecipeDao favoriteRecipeDao;

//    @Autowired
    private FavoriteRecipeMapper favoriteRecipeMapper;

//    @Autowired
    private UserDao userDao;

//    @Autowired
    private RecipeDao recipeDao;
    public List<FavoriteRecipeDto> getByUserId(Long userId){
        System.out.println("before service");
        return favoriteRecipeMapper.toDTOs(favoriteRecipeDao.getAllByUserId(userId));
    }
    public FavoriteRecipeDto create(FavoriteRecipeRequest favoriteRecipeRequest){
        return favoriteRecipeMapper.toDTO(favoriteRecipeDao.createOne(new FavoriteRecipeEntity(
                userDao.getByUserId(favoriteRecipeRequest.getUserId()),
                recipeDao.getByRecipeId(favoriteRecipeRequest.getRecipeId()),
                favoriteRecipeRequest.getNote())));
    }
}
