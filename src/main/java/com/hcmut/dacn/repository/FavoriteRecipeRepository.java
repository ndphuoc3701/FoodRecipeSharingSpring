package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.FavoriteRecipeEntity;
import com.hcmut.dacn.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipeEntity,Long> {
    Page<FavoriteRecipeEntity> findRecipesByUser_IdOrderByCreatedDateDesc(Long userId, Pageable pageable);
//    @Query("select f.recipe.id from FavoriteRecipeEntity f where f.user.id=:userId and f.recipe.id in :recipeIds")
//    List<Long> findRecipesByUser_Id(Long userId, List<Long> recipeIds);
    @Query("select f.recipe.id from FavoriteRecipeEntity f where f.user.id=:userId")
    List<Long> findRecipesByUser_Id(Long userId);

//    @Query("select f.recipe.id from FavoriteRecipeEntity f where f.user.id=:userId and f.recipe.id in :recipeIds")
    FavoriteRecipeEntity findRecipesByUser_IdAndRecipe_Id(Long userId, Long recipeId);
}
