package com.hcmut.dacn.controller;

import com.hcmut.dacn.dto.*;
import com.hcmut.dacn.entity.Product;
import com.hcmut.dacn.esRepo.ProductRepository;
import com.hcmut.dacn.esRepo.RecipeESRepository;
import com.hcmut.dacn.request.ScheduleRecipeRequest;
import com.hcmut.dacn.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("api/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("{recipeId}")
    public RecipeDetailDto getRecipeDetailById(@PathVariable Long recipeId){
        return recipeService.getRecipeDetailById(recipeId);
    }
    @PostMapping
    public RecipeDto create(@RequestBody RecipeSharingDto recipeRequest){
        return recipeService.create(recipeRequest);
    }
    @GetMapping
    public Pagination<RecipeDto> getRecipesByPage(@RequestParam int page){
        return recipeService.getAll(page);
    }
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

    @GetMapping("post")
    public Iterable<Product> getAll(){
        return productRepository.findAll();
    }

    @PostMapping("post")
    public Product create(@RequestBody Product product){
        return productRepository.save(product);
    }
}
