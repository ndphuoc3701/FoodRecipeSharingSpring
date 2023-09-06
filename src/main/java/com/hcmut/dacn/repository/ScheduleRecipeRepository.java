package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.RecipeEntity;
import com.hcmut.dacn.entity.ScheduleRecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRecipeRepository extends JpaRepository<ScheduleRecipeEntity,Long> {
    Page<ScheduleRecipeEntity> findScheduleRecipesByUser_IdOrderByScheduleTimeDesc(Long userId, Pageable pageable);
    ScheduleRecipeEntity findByUser_IdAndRecipe_Id(Long userId, Long recipeId);
}
