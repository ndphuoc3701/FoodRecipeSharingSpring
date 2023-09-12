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
import java.util.Objects;

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
        Integer oldNumEvaluation = recipe.getNumEvaluation();
        recipe.setNumEvaluation(oldNumEvaluation + 1);
        updateNumStarRecipe(recipe, evaluationRequest.getNumStar());
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
        updateEvaluationLevelOfUser(user, 5);

        LearntRecipeEntity learntRecipe = new LearntRecipeEntity();
        learntRecipe.setEvaluation(evaluation);
        learntRecipe.setUser(user);
        learntRecipe.setRecipe(recipe);
        evaluation.setLearntRecipe(learntRecipe);

        return evaluationMapper.toDto(evaluationRepository.save(evaluation));
    }

    public void likeOrDislikeEvaluation(Long evaluationId, boolean isLike) {
        EvaluationEntity evaluation = evaluationRepository.findById(evaluationId).orElse(null);
        int updateNumLike = 0;
        if (isLike) {
            evaluation.setNumLike(evaluation.getNumLike() + 1);
            if (Objects.equals(evaluation.getNumLike(), evaluation.getNumDislike()))
                updateNumLike = 5;
            else
                updateNumLike = 1;
        } else {
            evaluation.setNumDislike(evaluation.getNumDislike() + 1);
            if (Objects.equals(evaluation.getNumLike() + 1, evaluation.getNumDislike()))
                updateNumLike = -5;
            else
                updateNumLike = -1;
        }
        updateEvaluationLevelOfUser(evaluation.getUser(), updateNumLike);

        if (evaluation.getNumLike() >= evaluation.getNumDislike() - 1)
            updateNumStarRecipe(evaluation.getRecipe(), isLike ? evaluation.getNumStar() : -evaluation.getNumStar());
        evaluationRepository.save(evaluation);
    }

    private void updateNumStarRecipe(RecipeEntity recipe, Integer updatedNumStarEvaluation) {
        Integer updateNumLike = updatedNumStarEvaluation > 0 ? 1 : -1;
        int totalLikeRecipe = recipe.getNumLike() + updateNumLike;
        Double oldNumStarRecipe = recipe.getNumStar();
        if (totalLikeRecipe == 0)
            recipe.setNumStar(0.0);
        else
            recipe.setNumStar((recipe.getNumStar() * recipe.getNumLike() + updatedNumStarEvaluation) / totalLikeRecipe);
        recipe.setNumLike(totalLikeRecipe);
        int numberRecipeOfOwner = recipeRepository.numberRecipeOfUserId(recipe.getOwner().getId());
        recipe.getOwner().setCookLevel(((recipe.getOwner().getCookLevel() * numberRecipeOfOwner) + (recipe.getNumStar()-oldNumStarRecipe)) / numberRecipeOfOwner);
        RecipeDto recipeDto = recipeESRepository.findById(recipe.getId()).orElse(null);
        recipeDto.setNumStar(recipe.getNumStar());
        recipeDto.setNumEvaluation(recipe.getNumEvaluation());
        recipeESRepository.save(recipeDto);
    }

    private void updateEvaluationLevelOfUser(UserEntity user, int numLike) {
        user.setNumLike(user.getNumLike() + numLike);
        user.setEvaluationLevel((double) user.getNumLike() / 100);
    }
}
