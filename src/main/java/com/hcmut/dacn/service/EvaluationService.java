package com.hcmut.dacn.service;

import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.dto.Pagination;
import com.hcmut.dacn.dto.RecipeDto;
import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.esRepo.RecipeESRepository;
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
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeESRepository recipeESRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationMapper evaluationMapper;

    public Pagination<EvaluationDto> getEvaluationsByRecipeId(Long recipeId, int page) {
        Page<EvaluationEntity> evaluationEntityPage = evaluationRepository.findEvaluationsByRecipe_IdOrderByCreatedDateDesc(recipeId, PageRequest.of(page - 1, 5));
        List<EvaluationEntity> evaluations = evaluationEntityPage.getContent();
        List<EvaluationDto> evaluationDtos = evaluationMapper.toDtos(evaluations);
        Pagination<EvaluationDto> evaluationDtoPagination = new Pagination<>();
        evaluationDtoPagination.setTotalPages(evaluationEntityPage.getTotalPages());
        evaluationDtoPagination.setObjects(evaluationDtos);
        return evaluationDtoPagination;
    }

    public EvaluationDto create(EvaluationRequest evaluationRequest) {
        UserEntity user = userRepository.findById(evaluationRequest.getUserId()).orElse(null);
        RecipeEntity recipe = recipeRepository.findById(evaluationRequest.getRecipeId()).orElse(null);
        RecipeDto recipeDto = recipeESRepository.findById(evaluationRequest.getRecipeId()).orElse(null);
        Integer oldNumEvaluation = recipe.getNumEvaluation();
        recipe.setNumEvaluation(oldNumEvaluation + 1);
        recipeDto.setNumEvaluation(oldNumEvaluation + 1);
        Double newNumStar = (recipe.getNumStar() * (recipe.getNumEvaluation() - 1) + evaluationRequest.getNumStar()) / recipe.getNumEvaluation();
        recipe.setNumStar(newNumStar);
        recipeDto.setNumStar(newNumStar);
        int numberRecipeOfOwner = evaluationRepository.numberEvaluationOfUserId(recipe.getOwner().getId());
        recipe.getOwner().setCookLevel(((recipe.getOwner().getCookLevel() * numberRecipeOfOwner) + evaluationRequest.getNumStar()) / (numberRecipeOfOwner + 1));
        recipeRepository.save(recipe);
        recipeESRepository.save(recipeDto);
        EvaluationEntity evaluation = new EvaluationEntity();
        evaluation.setContent(evaluationRequest.getContent());
        evaluation.setNumStar(evaluationRequest.getNumStar());
        evaluation.setNote(evaluationRequest.getNote());
        evaluation.setUser(user);
        evaluation.setRecipe(recipe);
        List<ImageEntity> images = new ArrayList<>();
        evaluationRequest.getImages().forEach(i -> {
                    ImageEntity image = new ImageEntity();
                    image.setEvaluation(evaluation);
                    image.setData(i.getData().getBytes(StandardCharsets.UTF_8));
                    images.add(image);
                }
        );
        evaluation.setImages(images);

        LearntRecipeEntity learntRecipe = new LearntRecipeEntity();
        learntRecipe.setEvaluation(evaluation);
        learntRecipe.setUser(user);
        learntRecipe.setRecipe(recipe);
        evaluation.setLearntRecipe(learntRecipe);

        return evaluationMapper.toDto(evaluationRepository.save(evaluation));
    }
}
