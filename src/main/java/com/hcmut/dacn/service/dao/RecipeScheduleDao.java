package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.ScheduleRecipeEntity;

import java.util.List;

public interface RecipeScheduleDao {
    ScheduleRecipeEntity createOne(ScheduleRecipeEntity recipeSchedule);
//    RecipeScheduleEntity getByRecipeScheduleId(Long recipeScheduleId);
    List<ScheduleRecipeEntity> getAllByUserId(Long userId);
}
