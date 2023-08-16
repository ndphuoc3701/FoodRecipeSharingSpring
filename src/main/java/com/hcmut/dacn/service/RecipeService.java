package com.hcmut.dacn.service;

import com.hcmut.dacn.dto.*;
import com.hcmut.dacn.esRepo.ProductRepository;
import com.hcmut.dacn.esRepo.RecipeESRepository;
import com.hcmut.dacn.mapper.LearntRecipeMapper;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.mapper.ScheduleRecipeMapper;
import com.hcmut.dacn.repository.*;
import com.hcmut.dacn.request.ScheduleRecipeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hcmut.dacn.entity.*;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private ScheduleRecipeRepository scheduleRecipeRepository;
    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private LearntRecipeMapper learntRecipeMapper;
    @Autowired
    private ScheduleRecipeMapper scheduleRecipeMapper;
    @Autowired
    private RecipeESRepository recipeESRepository;
    @Autowired
    private ProductRepository productRepository;

    public Pagination<RecipeDto> getAll(String keyword, String filter, String ingredient, int page) {
        Page<RecipeDto> recipeDtoPage;

        if (!ingredient.isEmpty()) {
            String[] ingredientSplit = ingredient.split(" ");
            String queryIngredientReduce = Arrays.stream(ingredientSplit).reduce("", (curr, element) -> curr + "{\"match\":{\"ingredients\":" + element + "}},");
            String queryIngredient = queryIngredientReduce.substring(0, queryIngredientReduce.length() - 1);
            if (filter != null) {
                recipeDtoPage = recipeESRepository.getRecipesByKeywordAndFilter(keyword == null ? "" : keyword, queryIngredient, PageRequest.of(page - 1, 12, Sort.by(Sort.Order.desc(filter))));
            } else {
                recipeDtoPage = recipeESRepository.getRecipesByKeywordAndFilter(keyword == null ? "" : keyword, queryIngredient, PageRequest.of(page - 1, 12));
            }
        } else if (!keyword.isEmpty()) {
            String nfdNormalizedString = Normalizer.normalize(keyword, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            System.out.println(pattern.matcher(nfdNormalizedString).replaceAll(""));
            if (!filter.isEmpty()) {
                recipeDtoPage = recipeESRepository.getRecipesByKeyword(keyword, PageRequest.of(page - 1, 12, Sort.by(Sort.Order.desc(filter))));
            }
            else{
                recipeDtoPage = recipeESRepository.getRecipesByKeyword(keyword, PageRequest.of(page - 1, 12));
            }
        } else {
            if (!filter.isEmpty()) {
                recipeDtoPage = recipeESRepository.findAll(PageRequest.of(page - 1, 12, Sort.by(Sort.Order.desc(filter))));
            } else {
                recipeDtoPage = recipeESRepository.findAll(PageRequest.of(page - 1, 12));
            }
        }
        Pagination<RecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages(recipeDtoPage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtoPage.getContent());
        return recipeDtoPagination;

    }

    //    public RecipeDto getByRecipeId(Long recipeId){
//        return recipeMapper.toDto(recipeDao.getByRecipeId(recipeId));
//    }
    public Pagination<RecipeDto> getRecipesByUserId(Long userId, int page) {
        Page<RecipeEntity> recipeEntityPage = recipeRepository.findRecipesByOwner_IdOrderByCreatedDateDesc(userId, PageRequest.of(page - 1, 10));
        List<RecipeDto> recipeDtos = recipeMapper.toDtos(recipeEntityPage.getContent());
        Pagination<RecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages(recipeEntityPage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }

    public RecipeDto create(RecipeSharingDto recipeRequest) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName(recipeRequest.getName());
        recipe.setImageData(recipeRequest.getImage().getBytes(StandardCharsets.UTF_8));
        recipe.setOwner(userRepository.findById(recipeRequest.getOwnerId()).orElse(null));
        List<InstructionEntity> instructions = new ArrayList<>();
        recipeRequest.getInstructions().forEach(
                instructionRequest -> {
                    InstructionEntity instruction = new InstructionEntity();
                    instruction.setRecipe(recipe);
                    instruction.setContent(instructionRequest.getContent());
                    instruction.setStepOrder(instructionRequest.getStepOrder());
                    List<ImageEntity> images = new ArrayList<>();
                    instructionRequest.getImages().forEach(i -> {
                        ImageEntity image = new ImageEntity();
                        image.setInstruction(instruction);
                        image.setData(i.getData().getBytes(StandardCharsets.UTF_8));
                        images.add(image);
                    });
                    instruction.setImages(images);
                    instructions.add(instruction);
                }
        );
        recipe.setInstructions(instructions);
        List<IngredientRecipeEntity> ingredientRecipes = new ArrayList<>();
        recipeRequest.getIngredients().forEach(
                ingredientRecipeRequest -> {
                    IngredientRecipeEntity ingredientRecipe = new IngredientRecipeEntity();
                    ingredientRecipe.setRecipe(recipe);
                    ingredientRecipe.setName(ingredientRecipeRequest.getName());
                    ingredientRecipe.setQuantity(ingredientRecipeRequest.getQuantity());
                    ingredientRecipes.add(ingredientRecipe);
                }
        );
        recipe.setIngredientRecipes(ingredientRecipes);
//        recipeESRepository.save(recipeMapper.toEsDto(recipeRepository.save(recipe)));
        return recipeESRepository.save(recipeMapper.toEsDto(recipeRepository.save(recipe)));

    }

    public void addFavoriteRecipe(Long userId, Long recipeId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElse(null);
        FavoriteRecipeEntity favoriteRecipe = new FavoriteRecipeEntity();
        favoriteRecipe.setRecipe(recipe);
        favoriteRecipe.setUser(user);
        favoriteRecipeRepository.save(favoriteRecipe);
    }

    public Pagination<RecipeDto> getFavoriteRecipesByUserId(Long userId, int page) {
        Page<FavoriteRecipeEntity> favoriteRecipePage = favoriteRecipeRepository.findRecipesByUser_IdOrderByCreatedDateDesc(userId, PageRequest.of(page - 1, 10));
        List<RecipeDto> recipeDtos = new ArrayList<>();
        favoriteRecipePage.getContent().forEach(
                favoriteRecipeEntity -> {
                    recipeDtos.add(recipeMapper.toDto(favoriteRecipeEntity.getRecipe()));
                }
        );
        Pagination<RecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages(favoriteRecipePage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }

    public Pagination<LearntRecipeDto> getLearntRecipesByUserId(Long userId, int page) {
        Page<LearntRecipeEntity> learntRecipePage = learntRecipeRepository.findLearntRecipesByUser_IdOrderByEvaluation_CreatedDateDesc(userId, PageRequest.of(page - 1, 10));
        List<LearntRecipeEntity> learntRecipeEntities = learntRecipePage.getContent();
        List<LearntRecipeDto> learntRecipeDtos = learntRecipeMapper.toDTOs(learntRecipeEntities);
        for (int i = 0; i < learntRecipeEntities.size(); i++) {
            List<ImageDto> images = new ArrayList<>();
            learntRecipeEntities.get(i).getEvaluation().getImages().forEach(im -> {
                ImageDto image = new ImageDto();
                image.setData(new String(im.getData(), StandardCharsets.UTF_8));
                images.add(image);
            });
            learntRecipeDtos.get(i).getEvaluation().setImages(images);
        }

        Pagination<LearntRecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages(learntRecipePage.getTotalPages());
        recipeDtoPagination.setObjects(learntRecipeDtos);
        return recipeDtoPagination;
    }

    public RecipeDetailDto getRecipeDetailById(Long recipeId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElse(null);
        RecipeDetailDto recipeDetail = recipeMapper.toRecipeDetailDto(recipe);
        recipeDetail.getUser().setImage(new String(recipe.getOwner().getImageData(), StandardCharsets.UTF_8));
        return recipeDetail;
    }

    public Pagination<ScheduleRecipeDto> getScheduledRecipesByUserId(Long userId, int page) {
        Page<ScheduleRecipeEntity> scheduleRecipePage = scheduleRecipeRepository.findScheduleRecipesByUser_IdOrderByScheduleTimeDesc(userId, PageRequest.of(page - 1, 10));
        List<ScheduleRecipeEntity> scheduleRecipeEntities = scheduleRecipePage.getContent();
        List<ScheduleRecipeDto> scheduleRecipeDtos = scheduleRecipeMapper.toDtos(scheduleRecipeEntities);
        Pagination<ScheduleRecipeDto> scheduleRecipeDtoPagination = new Pagination<>();
        scheduleRecipeDtoPagination.setTotalPages(scheduleRecipePage.getTotalPages());
        scheduleRecipeDtoPagination.setObjects(scheduleRecipeDtos);
        return scheduleRecipeDtoPagination;
    }

    public ScheduleRecipeDto scheduleRecipe(ScheduleRecipeRequest scheduleRecipeRequest) {
        UserEntity user = userRepository.findById(scheduleRecipeRequest.getUserId()).orElse(null);
        RecipeEntity recipe = recipeRepository.findById(scheduleRecipeRequest.getRecipeId()).orElse(null);
        ScheduleRecipeEntity scheduleRecipe = new ScheduleRecipeEntity();
        scheduleRecipe.setUser(user);
        scheduleRecipe.setRecipe(recipe);
        scheduleRecipe.setScheduleTime(scheduleRecipeRequest.getScheduleTime());
        scheduleRecipe.setNote(scheduleRecipeRequest.getNote());
        return scheduleRecipeMapper.toDto(scheduleRecipeRepository.save(scheduleRecipe));
    }

    public Pagination<RecipeDto> getRecipesByKeyword(String keyword, int page) {
        Page<RecipeDto> recipeESPage = recipeESRepository.getRecipesByKeyword(keyword, PageRequest.of(page - 1, 12));
        Pagination<RecipeDto> recipeESPagination = new Pagination<>();
        recipeESPagination.setTotalPages(recipeESPage.getTotalPages());
        recipeESPagination.setObjects(recipeESPage.getContent());
        return recipeESPagination;
    }

    public List<String> getRecipesByKeywordSearchBar(String keyword) {
        Page<RecipeDto> recipeESPage = recipeESRepository.getRecipesByKeyword(keyword, PageRequest.of(0, 10));
//        recipeESPagination.setObjects(recipeESPage.getContent());
        List<String> recipeNames = new ArrayList<>();
        recipeNames = recipeESPage.getContent().stream().map(RecipeDto::getName).collect(Collectors.toList());
        return recipeNames;
    }

}
