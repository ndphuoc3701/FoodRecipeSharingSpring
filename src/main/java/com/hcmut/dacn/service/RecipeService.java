package com.hcmut.dacn.service;

import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.ImageInstructionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dao.*;
import com.hcmut.dacn.service.dto.*;
import com.hcmut.dacn.service.mapper.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

//    @Autowired
    private RecipeDao recipeDao;

//    @Autowired
    private UserDao userDao;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeMapper recipeMapper;
    public List<RecipeDto> getAll(){
        return recipeMapper.toDtos(recipeDao.getAll());
    }
    public List<RecipeDto> getByOwnerId(Long ownerId){
        return recipeMapper.toDtos(recipeDao.getAllByOwnerId(ownerId));
    }
    public RecipeDto getByRecipeId(Long recipeId){
        return recipeMapper.toDto(recipeDao.getByRecipeId(recipeId));
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
//                    instructionRequest.getImages().forEach(
//                            imageInstructionRequest -> {
//                                ImageInstructionEntity imageInstruction=new ImageInstructionEntity();
//                                imageInstruction.setInstruction(instruction);
//                                imageInstruction.setImageData(imageInstructionRequest.getImage().getBytes(StandardCharsets.UTF_8));
//                                imageInstructions.add(imageInstruction);
//                            }
//                    );
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

    public void cr(ImageInstructionRequest imageInstructionRequest){
        ImageInstructionEntity imageInstruction=new ImageInstructionEntity();
        imageInstruction.setImageData(imageInstructionRequest.getImage().getBytes(StandardCharsets.UTF_8));
        String s= new String(imageInstruction.getImageData(),StandardCharsets.UTF_8);
    }

}
