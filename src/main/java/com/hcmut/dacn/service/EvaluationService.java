package com.hcmut.dacn.service;

import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.dto.Pagination;
import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.repository.EvaluationRepository;
import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.dto.EvaluationDto;
import com.hcmut.dacn.mapper.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public Pagination<EvaluationDto> getEvaluationsByRecipeId(Long recipeId, int page){
        Page<EvaluationEntity> evaluationEntityPage=evaluationRepository.findEvaluationsByRecipe_Id(recipeId,PageRequest.of(page-1,10));
        List<EvaluationEntity> evaluations=evaluationEntityPage.getContent();
        List<EvaluationDto> evaluationDtos=evaluationMapper.toDtos(evaluations);
        for(int i=0;i<evaluations.size();i++){
            ImageDto image=new ImageDto();
            image
            image.setData(evaluations.get(i).g);
        }
        Pagination<EvaluationDto> evaluationDtoPagination=new Pagination<>();
        evaluationDtoPagination.setTotalPages(evaluationEntityPage.getTotalPages());
        evaluationDtoPagination.setObjects(evaluationDtos);
        return evaluationDtoPagination;
    }

//    public List<EvaluationUserDto> getEvaluationsByUserId(Long userId, int page){
//        return evaluationMapper.toUserDtos(evaluationRepository.findEvaluationsByUser_Id(userId, PageRequest.of(page-1,10)).getContent());
//    }

//    public List<EvaluationRecipeDto> getEvaluationsByUserId(Long userId, int page){
//        return evaluationMapper.toUserDtos(evaluationRepository.findEvaluationsByUser_Id(userId, PageRequest.of(page-1,10)).getContent());
//    }

    public EvaluationDto create(EvaluationRequest evaluationRequest){
        UserEntity user=userRepository.findById(evaluationRequest.getUserId()).orElse(null);
        RecipeEntity recipe=recipeRepository.findById(evaluationRequest.getRecipeId()).orElse(null);
        EvaluationEntity evaluation=new EvaluationEntity();
        evaluation.setContent(evaluationRequest.getContent());
        evaluation.setNumStar(evaluationRequest.getNumStar());
        evaluation.setNote(evaluationRequest.getNote());
        evaluation.setUser(user);
        evaluation.setRecipe(recipe);
        List<ImageEntity> images=new ArrayList<>();
        evaluationRequest.getImages().forEach(i->{
            ImageEntity image=new ImageEntity();
            image.setEvaluation(evaluation);
            image.setData(i.getData().getBytes(StandardCharsets.UTF_8));
            images.add(image);
                }
        );
        evaluation.setImages(images);

        LearntRecipeEntity learntRecipe=new LearntRecipeEntity();
        learntRecipe.setEvaluation(evaluation);
        learntRecipe.setUser(user);
        learntRecipe.setRecipe(recipe);
        evaluation.setLearntRecipe(learntRecipe);
        return evaluationMapper.toDto(evaluationRepository.save(evaluation));
    }
}
