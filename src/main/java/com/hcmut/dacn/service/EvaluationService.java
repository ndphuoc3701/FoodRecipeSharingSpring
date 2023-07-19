package com.hcmut.dacn.service;

import com.hcmut.dacn.entity.EvaluationEntity;
import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.service.dao.EvaluationDao;
import com.hcmut.dacn.service.dao.RecipeDao;
import com.hcmut.dacn.service.dao.UserDao;
import com.hcmut.dacn.service.dto.EvaluationDto;
import com.hcmut.dacn.service.mapper.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

//    @Autowired
    EvaluationDao evaluationDao;

//    @Autowired
    RecipeDao recipeDao;

//    @Autowired
    UserDao userDao;

    @Autowired
    EvaluationMapper evaluationMapper;
    public List<EvaluationDto> getAllByRecipeId(Long recipeId){
        return evaluationMapper.toDtos(evaluationDao.getAllByRecipeId(recipeId));
    }

    public EvaluationDto create(EvaluationRequest evaluationRequest){
        EvaluationEntity evaluation=new EvaluationEntity();
        evaluation.setContent(evaluationRequest.getContent());
        evaluation.setNumStart(evaluationRequest.getNumStart());
        evaluation.setUser(userDao.getByUserId(evaluationRequest.getUserId()));
        evaluation.setRecipe(recipeDao.getByRecipeId(evaluationRequest.getRecipeId()));
        return evaluationMapper.toDto(evaluationDao.create(evaluation));
    }
}
