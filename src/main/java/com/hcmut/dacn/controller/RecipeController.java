package com.hcmut.dacn.controller;

import com.hcmut.dacn.request.ImageInstructionRequest;
import com.hcmut.dacn.service.RecipeService;
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

    @GetMapping
    public List<RecipeDto> getAll(){
        return recipeService.getAll();
    }

    @GetMapping("query")
    public List<RecipeDto> getByOwnerId(@RequestParam("ownerId") Long ownerId){
        return recipeService.getByOwnerId(ownerId);
    }
    @GetMapping("{recipeId}")
    public RecipeDto getByRecipeId(@PathVariable("recipeId") Long recipeId){
//        try {
//            return recipeService.getByRecipeId(recipeId);
//        }
//        catch (FoodRecipeSharingException e){
//            return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
//        }
        return recipeService.getByRecipeId(recipeId);
    }

    @PostMapping
    public void create(@RequestBody RecipeSharingDto recipeRequest){
        recipeService.create(recipeRequest);
    }

    @PostMapping("lol")
    public void cr(@RequestBody ImageInstructionRequest imageInstructionRequest){
         recipeService.cr(imageInstructionRequest);
    }

    @GetMapping("cc")
    public String c(){
        return "cc";
    }
}
