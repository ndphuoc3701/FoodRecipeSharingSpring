package com.hcmut.dacn.controller;

import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.service.EvaluationService;
import com.hcmut.dacn.service.dto.EvaluationRecipeDto;
import com.hcmut.dacn.service.dto.EvaluationUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/evaluations")
public class EvaluationController {

    @Autowired
    EvaluationService evaluationService;

    @GetMapping("recipes/{recipeId}")
    public List<EvaluationRecipeDto> getEvaluationsByRecipeId(@PathVariable Long recipeId, @RequestParam int page){
        return evaluationService.getEvaluationsByRecipeId(recipeId,page);
    }

    @GetMapping("users/{userId}")
    public List<EvaluationUserDto> getEvaluationsByUserId(@PathVariable Long userId, @RequestParam int page){
        return evaluationService.getEvaluationsByUserId(userId,page);
    }

    @PostMapping
    public EvaluationRecipeDto create(@RequestBody EvaluationRequest evaluationRequest){
        return evaluationService.create(evaluationRequest);
    }
}
