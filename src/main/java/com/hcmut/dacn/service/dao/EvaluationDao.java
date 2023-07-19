package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.EvaluationEntity;

import java.util.List;

public interface EvaluationDao {
    List<EvaluationEntity> getAllByRecipeId(Long recipeId);
    EvaluationEntity create(EvaluationEntity evaluation);
    EvaluationEntity getByEvaluationId(Long evaluationId);
}
