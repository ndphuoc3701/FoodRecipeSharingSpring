package com.hcmut.dacn.service;

import com.hcmut.dacn.entity.EvaluationEntity;
import com.hcmut.dacn.entity.LearntRecipeEntity;
import com.hcmut.dacn.repository.EvaluationRepository;
import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.service.dao.EvaluationDao;
import com.hcmut.dacn.service.dao.RecipeDao;
import com.hcmut.dacn.service.dao.UserDao;
import com.hcmut.dacn.service.dto.EvaluationRecipeDto;
import com.hcmut.dacn.service.dto.EvaluationUserDto;
import com.hcmut.dacn.service.mapper.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EvaluationMapper evaluationMapper;
    public List<EvaluationRecipeDto> getEvaluationsByRecipeId(Long recipeId, int page){
        return evaluationMapper.toRecipeDtos(evaluationRepository.findEvaluationsByRecipe_Id(recipeId, PageRequest.of(page-1,10)).getContent());
    }
    public List<EvaluationUserDto> getEvaluationsByUserId(Long userId, int page){
        return evaluationMapper.toUserDtos(evaluationRepository.findEvaluationsByUser_Id(userId, PageRequest.of(page-1,10)).getContent());
    }

    public EvaluationRecipeDto create(EvaluationRequest evaluationRequest){
        EvaluationEntity evaluation=new EvaluationEntity();
        evaluation.setContent(evaluationRequest.getContent());
        evaluation.setNumStar(evaluationRequest.getNumStar());
        evaluation.setNote(evaluationRequest.getNote());
        evaluation.setUser(userRepository.findById(evaluationRequest.getUserId()).orElse(null));
        evaluation.setRecipe(recipeRepository.findById(evaluationRequest.getRecipeId()).orElse(null));
        return evaluationMapper.toRecipeDto(evaluationRepository.save(evaluation));
    }
}
