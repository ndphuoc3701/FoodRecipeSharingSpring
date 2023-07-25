package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.FavoriteRecipeEntity;
import com.hcmut.dacn.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipeEntity,Long> {
    Page<FavoriteRecipeEntity> findRecipesByUser_Id(Long userId, Pageable pageable);

}
