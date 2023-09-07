package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.RecipeEntity;
import com.hcmut.dacn.entity.ScheduleRecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ScheduleRecipeRepository extends JpaRepository<ScheduleRecipeEntity,Long> {


//    Page<ScheduleRecipeEntity> findScheduleRecipesByUser_IdOrderByScheduleTimeDesc(Long userId, Pageable pageable);
    @Query("select s from ScheduleRecipeEntity s where s.user.id =:userId and s.scheduleTime >= :currentTime order by s.scheduleTime asc")
    Page<ScheduleRecipeEntity> findByUser_Id(Long userId, Date currentTime, Pageable pageable);

    @Query("select s from ScheduleRecipeEntity s where s.user.id =:userId and s.scheduleTime < :currentTime order by s.scheduleTime desc")
    Page<ScheduleRecipeEntity> findOldByUser_Id(Long userId, Date currentTime, Pageable pageable);
    ScheduleRecipeEntity findByUser_IdAndRecipe_Id(Long userId, Long recipeId);
}
