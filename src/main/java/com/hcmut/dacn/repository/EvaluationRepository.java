package com.hcmut.dacn.repository;

import com.hcmut.dacn.entity.EvaluationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<EvaluationEntity,Long> {
    Page<EvaluationEntity> findEvaluationsByRecipe_IdOrderByCreatedDateDesc(Long recipeId, Pageable pageable);
    Page<EvaluationEntity> findEvaluationsByUser_Id(Long userId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM EvaluationEntity e WHERE e.recipe.owner.id=:userId")
    int numberEvaluationOfUserId(Long userId);
}
