package controller;

import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.service.EvaluationService;
import com.hcmut.dacn.service.dto.EvaluationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("evaluations")
public class EvaluationController {

    @Autowired
    EvaluationService evaluationService;

    @GetMapping("query")
    public List<EvaluationDto> getByUserId(@RequestParam("recipeId") Long recipeId){
        return evaluationService.getAllByRecipeId(recipeId);
    }

    @PostMapping
    public EvaluationDto create(EvaluationRequest evaluationRequest){
        return evaluationService.create(evaluationRequest);
    }
}
