package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity,Long> {
//    List<RecipeEntity> findRecipeByPage(Pagea)
    Page<RecipeEntity> findRecipesByOwner_IdOrderByCreatedDateDesc(Long userId, Pageable pageable);
//    @Query("select r from RecipeEntity r where r.name like %:name")
    List<RecipeEntity> findByNameContains(String title);

    @Query("SELECT COUNT(*) FROM RecipeEntity e WHERE e.owner.id=:userId")
    int numberRecipeOfUserId(Long userId);
}
