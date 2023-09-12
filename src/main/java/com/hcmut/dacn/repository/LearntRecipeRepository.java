package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.FavoriteRecipeEntity;
import com.hcmut.dacn.entity.LearntRecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearntRecipeRepository extends JpaRepository<LearntRecipeEntity,Long> {
    Page<LearntRecipeEntity> findLearntRecipesByUser_IdOrderByEvaluation_CreatedDateDesc(Long userId, Pageable pageable);
    LearntRecipeEntity findByUser_IdAndRecipe_Id(Long userId,Long recipeId);
}
