package com.hcmut.dacn.service;

import com.hcmut.dacn.repository.FavoriteRecipeRepository;
import com.hcmut.dacn.repository.LearntRecipeRepository;
import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.FavoriteRecipeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import com.hcmut.dacn.service.mapper.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @Autowired
    private LearntRecipeRepository learntRecipeRepository;
    @Autowired
    private RecipeMapper recipeMapper;

    public Pagination<RecipeDto> getAll(int page){
        Page<RecipeEntity> recipeEntityPage=recipeRepository.findAll(PageRequest.of(page-1,12));
//        return recipeMapper.toDtos(recipeRepository.findAll(PageRequest.of(page-1,12)).getContent());
        List<RecipeDto> recipeDtos=recipeMapper.toDtos(recipeEntityPage.getContent());
        Pagination<RecipeDto> recipeDtoPagination=new Pagination<>();
        recipeDtoPagination.setTotalPages(recipeEntityPage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;

    }

//    public RecipeDto getByRecipeId(Long recipeId){
//        return recipeMapper.toDto(recipeDao.getByRecipeId(recipeId));
//    }
    public Pagination<RecipeDto> getRecipesByUserId(Long userId,int page){
        Page<RecipeEntity> recipeEntityPage=recipeRepository.findRecipesByOwner_Id(userId,PageRequest.of(page-1,10));
        List<RecipeDto> recipeDtos=recipeMapper.toDtos(recipeEntityPage.getContent());
        Pagination<RecipeDto> recipeDtoPagination=new Pagination<>();
        recipeDtoPagination.setTotalPages(recipeEntityPage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }
    public void create(RecipeSharingDto recipeRequest){
        RecipeEntity recipe=new RecipeEntity();
        recipe.setName(recipeRequest.getName());
        recipe.setImageData(recipeRequest.getImage().getBytes(StandardCharsets.UTF_8));
        recipe.setOwner(userRepository.findById(recipeRequest.getOwnerId()).orElse(null));
        List<InstructionEntity> instructions=new ArrayList<>();
        recipeRequest.getInstructions().forEach(
                instructionRequest -> {
                    InstructionEntity instruction=new InstructionEntity();
                    instruction.setRecipe(recipe);
                    instruction.setContent(instructionRequest.getContent());
                    instruction.setStepOrder(instructionRequest.getStepOrder());
                    List<ImageInstructionEntity> imageInstructions=new ArrayList<>();
                    for(int i=0;i<instructionRequest.getImages().length;i++){
                        ImageInstructionEntity imageInstruction=new ImageInstructionEntity();
                        imageInstruction.setInstruction(instruction);
                        imageInstruction.setImageData(instructionRequest.getImages()[i].getBytes(StandardCharsets.UTF_8));
                        imageInstructions.add(imageInstruction);
                    }
                    instruction.setImageInstructions(imageInstructions);
                    instructions.add(instruction);
                }
        );
        recipe.setInstructions(instructions);
        List<IngredientRecipeEntity> ingredientRecipes=new ArrayList<>();
        recipeRequest.getIngredients().forEach(
                ingredientRecipeRequest -> {
                    IngredientRecipeEntity ingredientRecipe=new IngredientRecipeEntity();
                    ingredientRecipe.setRecipe(recipe);
                    ingredientRecipe.setName(ingredientRecipeRequest.getName());
                    ingredientRecipe.setQuantity(ingredientRecipeRequest.getQuantity());
                    ingredientRecipes.add(ingredientRecipe);
                }
        );
        recipe.setIngredientRecipes(ingredientRecipes);
        recipeRepository.save(recipe);
    }

    public void addFavoriteRecipe(Long userId,Long recipeId) {
        UserEntity user=userRepository.findById(userId).orElse(null);
        RecipeEntity recipe=recipeRepository.findById(recipeId).orElse(null);
        FavoriteRecipeEntity favoriteRecipe=new FavoriteRecipeEntity();
        favoriteRecipe.setRecipe(recipe);
        favoriteRecipe.setUser(user);
        favoriteRecipeRepository.save(favoriteRecipe);
    }

    public Pagination<RecipeDto> getFavoriteRecipesByUserId(Long userId,int page){
        Page<FavoriteRecipeEntity> favoriteRecipePage =favoriteRecipeRepository.findRecipesByUser_Id(userId,PageRequest.of(page-1,10));
        List<RecipeDto> recipeDtos=new ArrayList<>();
        favoriteRecipePage.getContent().forEach(
                favoriteRecipeEntity -> {
                    recipeDtos.add(recipeMapper.toDto(favoriteRecipeEntity.getRecipe()));
                }
        );
        Pagination<RecipeDto> recipeDtoPagination=new Pagination<>();
        recipeDtoPagination.setTotalPages(favoriteRecipePage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }

    public Pagination<RecipeDto> getLearntRecipesByUserId(Long userId,int page){
        Page<LearntRecipeEntity> learntRecipePage =learntRecipeRepository.findLearntRecipesByUser_Id(userId,PageRequest.of(page-1,10));
        List<RecipeDto> recipeDtos=new ArrayList<>();
        learntRecipePage.getContent().forEach(
                favoriteRecipeEntity -> {
                    recipeDtos.add(recipeMapper.toDto(favoriteRecipeEntity.getRecipe()));
                }
        );
        Pagination<RecipeDto> recipeDtoPagination=new Pagination<>();
        recipeDtoPagination.setTotalPages(learntRecipePage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }
}
