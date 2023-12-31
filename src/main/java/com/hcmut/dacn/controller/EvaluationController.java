package com.hcmut.dacn.controller;

import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.dto.Pagination;
import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.service.EvaluationService;
import com.hcmut.dacn.dto.EvaluationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("api/evaluations")
public class EvaluationController {

    @Autowired
    EvaluationService evaluationService;

    @GetMapping("recipes/{recipeId}")
    public Pagination<EvaluationDto> getEvaluationsByRecipeId(@PathVariable Long recipeId, @RequestParam int page){
        return evaluationService.getEvaluationsByRecipeId(recipeId,page);
    }

    @PostMapping
    public EvaluationDto create(@RequestBody EvaluationRequest evaluationRequest){
        return evaluationService.create(evaluationRequest);
    }

    @GetMapping("{evaluationId}")
    public void likeOrDislikeEvaluation(@PathVariable Long evaluationId, @RequestParam boolean isLike){
        evaluationService.likeOrDislikeEvaluation(evaluationId,isLike);
    }
}
