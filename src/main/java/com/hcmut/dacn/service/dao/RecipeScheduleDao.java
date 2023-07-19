package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.RecipeScheduleEntity;

import java.util.List;

public interface RecipeScheduleDao {
    RecipeScheduleEntity createOne(RecipeScheduleEntity recipeSchedule);
//    RecipeScheduleEntity getByRecipeScheduleId(Long recipeScheduleId);
    List<RecipeScheduleEntity> getAllByUserId(Long userId);
}
