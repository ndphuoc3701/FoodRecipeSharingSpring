package com.hcmut.dacn.controller;

import com.hcmut.dacn.request.FavoriteRecipeRequest;
import com.hcmut.dacn.request.ImageInstructionRequest;
import com.hcmut.dacn.service.RecipeService;
import com.hcmut.dacn.service.dto.Pagination;
import com.hcmut.dacn.service.dto.RecipeDto;
import com.hcmut.dacn.service.dto.RecipeSharingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("api/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

//    @GetMapping
//    public List<RecipeDto> getAll(){
//        return recipeService.getAll();
//    }

//    @GetMapping("query")
//    public List<RecipeDto> getByOwnerId(@RequestParam("ownerId") Long ownerId){
//        return recipeService.getByOwnerId(ownerId);
//    }
//    @GetMapping("{recipeId}")
//    public RecipeDto getByRecipeId(@PathVariable("recipeId") Long recipeId){
//        return recipeService.getByRecipeId(recipeId);
//    }
    @PostMapping
    public void create(@RequestBody RecipeSharingDto recipeRequest){
        recipeService.create(recipeRequest);
    }
    @GetMapping
    public Pagination<RecipeDto> getRecipesByPage(@RequestParam int page){
        return recipeService.getAll(page);
    }
    @GetMapping("user-recipes")
    public Pagination<RecipeDto> getRecipesByUserId(@RequestParam Long userId,@RequestParam int page){
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
    public Pagination<RecipeDto> getLearntRecipesByUserId(@RequestParam Long userId,@RequestParam int page){
        return recipeService.getLearntRecipesByUserId(userId,page);
    }

    @GetMapping("cc")
    public String getRecipesByPages(){
        return "ol";
    }

    @GetMapping("dd")
    public String getRecipesByPagess(){
        return "olc";
    }
}
