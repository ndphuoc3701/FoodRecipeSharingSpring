package controller;


import com.hcmut.dacn.request.RecipeScheduleRequest;
import com.hcmut.dacn.service.RecipeScheduleService;
import com.hcmut.dacn.service.dto.RecipeScheduleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recipe-schedules")
public class RecipeScheduleController {
    public final static String PATH="recipe-schedules";
    @Autowired
    RecipeScheduleService recipeScheduleService;

//    @GetMapping("{recipeScheduleId}")
//    public RecipeScheduleDto getByRecipeScheduleId(@PathVariable("recipeScheduleId") Long recipeScheduleId){
//        return recipeScheduleService.getByRecipeScheduleId(recipeScheduleId);
//    }

    @PostMapping
    public RecipeScheduleDto create(RecipeScheduleRequest recipeScheduleRequest){
        return recipeScheduleService.create(recipeScheduleRequest);
    }

    @GetMapping("query")
    public List<RecipeScheduleDto> getAllByUserId(@RequestParam("userId") Long userId){
        return recipeScheduleService.getAllByUserId(userId);
    }
}
