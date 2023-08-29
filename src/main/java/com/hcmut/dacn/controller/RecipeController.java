package com.hcmut.dacn.controller;

import com.hcmut.dacn.dto.*;
import com.hcmut.dacn.request.Message;
import com.hcmut.dacn.request.Schedule;
import com.hcmut.dacn.request.ScheduleRecipeRequest;
import com.hcmut.dacn.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("api/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("{recipeId}")
    public RecipeDetailDto getRecipeDetailById(@PathVariable Long recipeId){
        return recipeService.getRecipeDetailById(recipeId);
    }
    @PostMapping
    public RecipeDto create(@RequestBody RecipeSharingDto recipeRequest){
        return recipeService.create(recipeRequest);
    }

    @PostMapping("list")
    public List<RecipeDto> createList(@RequestBody List<RecipeSharingDto> recipeRequests){
        return recipeService.createList(recipeRequests);
    }
    @GetMapping
    public Pagination<RecipeDto> getRecipesByPage(@RequestParam String keyword, @RequestParam String filter, @RequestParam String ingredient, @RequestParam int page){
        return recipeService.getAll(keyword, filter, ingredient, page);
    }

    @GetMapping("search")
    public List<String> getRecipesByInput(@RequestParam String keyword){
        return recipeService.getRecipeByInput(keyword);
    }
//    @GetMapping
//    public void getRecipesByPage(@RequestParam String keyword, @RequestParam String filter, @RequestParam String ingredient, @RequestParam int page) throws IOException {
//        recipeService.getAll(keyword,);
//    }
    @GetMapping("users/{userId}")
    public Pagination<RecipeDto> getRecipesByUserId(@PathVariable Long userId,@RequestParam int page){
        return recipeService.getRecipesByUserId(userId,page);
    }
    @PostMapping("favorite")
    public void addFavoriteRecipe(@RequestParam Long userId,@RequestParam Long recipeId){
        recipeService.addFavoriteRecipe(userId,recipeId);
    }
    @GetMapping("favorite")
    public Pagination<RecipeDto> getFavoriteRecipesByUserId(@RequestParam Long userId,@RequestParam int page){
        return recipeService.getFavoriteRecipesByUserId(userId,page);
    }
    @GetMapping("learning")
    public Pagination<LearntRecipeDto> getLearntRecipesByUserId(@RequestParam Long userId, @RequestParam int page){
        return recipeService.getLearntRecipesByUserId(userId,page);
    }
    @GetMapping("scheduling")
    public Pagination<ScheduleRecipeDto> getScheduledRecipesByUserId(@RequestParam Long userId, @RequestParam int page){
        return recipeService.getScheduledRecipesByUserId(userId,page);
    }
    @PostMapping("scheduling")
    public void scheduleRecipe(@RequestBody ScheduleRecipeRequest scheduleRecipeRequest){
        recipeService.scheduleRecipe(scheduleRecipeRequest);
    }

//    @MessageMapping("/application")
//    @SendTo("/all/messages")
//    public Message send(final Message message) throws Exception {
//        return message;
//    }

}
