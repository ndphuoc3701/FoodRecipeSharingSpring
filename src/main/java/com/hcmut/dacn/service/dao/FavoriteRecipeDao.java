package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.FavoriteRecipeEntity;

import java.util.List;
public interface FavoriteRecipeDao {
    List<FavoriteRecipeEntity> getAllByUserId(Long userId);
    FavoriteRecipeEntity createOne(FavoriteRecipeEntity favoriteRecipeRequest);
}
