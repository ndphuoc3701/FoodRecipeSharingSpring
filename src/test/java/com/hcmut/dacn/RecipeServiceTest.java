package com.hcmut.dacn;

import com.hcmut.dacn.dto.*;
import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.mapper.LearntRecipeMapper;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.mapper.ScheduleRecipeMapper;
import com.hcmut.dacn.repository.*;
import com.hcmut.dacn.request.ScheduleRecipeRequest;
import com.hcmut.dacn.service.RecipeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceTest {
    @Autowired
    private RecipeService recipeService;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ScheduleRecipeRepository scheduleRecipeRepository;
    @MockBean
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @MockBean
    private LearntRecipeRepository learntRecipeRepository;
    @MockBean
    private RecipeMapper recipeMapper;
    @MockBean
    private LearntRecipeMapper learntRecipeMapper;
    @MockBean
    private ScheduleRecipeMapper scheduleRecipeMapper;

    private static final String NAME_RECIPE="Bun Bo";
    private static final String IMAGE_RECIPE="image";
    private static final Long RECIPE_ID=1L;
    private static final Long USER_ID=1L;

    private static RecipeEntity recipe;
    private static UserEntity user;

    @Before
    public void initData(){
        user = new UserEntity();
        recipe = new RecipeEntity();
        recipe.setName(NAME_RECIPE);
        recipe.setImageData(IMAGE_RECIPE.getBytes(StandardCharsets.UTF_8));
        recipe.setOwner(user);
        recipe.setInstructions(new ArrayList<>());
        recipe.setIngredientRecipes(new ArrayList<>());
    }

    @Test
    public void testCreateRecipe(){
        RecipeSharingDto recipeRequest = new RecipeSharingDto();
        recipeRequest.setName(NAME_RECIPE);
        recipeRequest.setImage(IMAGE_RECIPE);
        recipeRequest.setOwnerId(USER_ID);
        InstructionDto instructionDto=new InstructionDto();
        ImageDto imageDto = new ImageDto();
        imageDto.setData("image");
        instructionDto.setImages(Arrays.asList(imageDto));
        recipeRequest.setInstructions(Arrays.asList(instructionDto));
        IngredientRecipeDto ingredientRecipeDto=new IngredientRecipeDto();
        recipeRequest.setIngredients(Arrays.asList(ingredientRecipeDto));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipe);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName(NAME_RECIPE);
        recipeDto.setImage("image");
        when(recipeMapper.toEsDto(recipe)).thenReturn(recipeDto);
        RecipeDto recipeDtoResult = recipeService.create(recipeRequest);

        Assert.assertEquals(recipeDtoResult.getName(),recipeRequest.getName());
        Assert.assertEquals(recipeDtoResult.getImage(),recipeRequest.getImage());
    }

    @Test
    public void testAddFavorite(){
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        FavoriteRecipeEntity favoriteRecipe=new FavoriteRecipeEntity();
        ArgumentCaptor<FavoriteRecipeEntity> recipeEntityArgumentCaptor=ArgumentCaptor.forClass(FavoriteRecipeEntity.class);
        doReturn(favoriteRecipe).when(favoriteRecipeRepository).save(recipeEntityArgumentCaptor.capture());
        recipeService.addFavoriteRecipe(USER_ID,RECIPE_ID);
        Assert.assertEquals(recipeEntityArgumentCaptor.getValue().getRecipe(),recipe);
        Assert.assertEquals(recipeEntityArgumentCaptor.getValue().getUser(),user);
        verify(favoriteRecipeRepository,times(1)).save(any(FavoriteRecipeEntity.class));
    }

    @Test
    public void testDeleteFavorite(){
        FavoriteRecipeEntity favoriteRecipe=new FavoriteRecipeEntity();
        favoriteRecipe.setRecipe(recipe);
        favoriteRecipe.setUser(user);
        when(favoriteRecipeRepository.findRecipesByUser_IdAndRecipe_Id(USER_ID,RECIPE_ID)).thenReturn(favoriteRecipe);
        recipeService.deleteFavoriteRecipe(USER_ID,RECIPE_ID);
        verify(favoriteRecipeRepository,times(1)).delete(favoriteRecipe);
    }

    @Test
    public void testGetFavoriteRecipesByUserId(){
        List<RecipeEntity> recipes = new ArrayList<>();
        RecipeEntity recipe1=new RecipeEntity();
        recipe1.setName("name1");
        RecipeEntity recipe2=new RecipeEntity();
        recipe2.setName("name2");
        recipes.add(recipe1);
        recipes.add(recipe2);
        List<FavoriteRecipeEntity> favoriteRecipes=new ArrayList<>();
        FavoriteRecipeEntity favoriteRecipe1= new FavoriteRecipeEntity();
        favoriteRecipe1.setRecipe(recipe1);
        FavoriteRecipeEntity favoriteRecipe2= new FavoriteRecipeEntity();
        favoriteRecipe2.setRecipe(recipe2);
        favoriteRecipes.add(favoriteRecipe1);
        favoriteRecipes.add(favoriteRecipe2);
        Page<FavoriteRecipeEntity> favoriteRecipePage=new PageImpl<FavoriteRecipeEntity>(favoriteRecipes);
        when(favoriteRecipeRepository.findRecipesByUser_IdOrderByCreatedDateDesc(USER_ID, PageRequest.of(0, 10))).thenReturn(favoriteRecipePage);

        List<RecipeDto> recipeDtos=new ArrayList<>();
        RecipeDto recipeDto1=new RecipeDto();
        recipeDto1.setName("name1");
        RecipeDto recipeDto2=new RecipeDto();
        recipeDto2.setName("name2");
        recipeDtos.add(recipeDto1);
        recipeDtos.add(recipeDto2);
        when(recipeMapper.toDtos(recipes)).thenReturn(recipeDtos);
        Pagination<RecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages(favoriteRecipePage.getTotalPages());
        recipeDtoPagination.setObjects(recipeDtos);
        Pagination<RecipeDto> recipeDtoPaginationActual=recipeService.getFavoriteRecipesByUserId(USER_ID,1);
        Assert.assertEquals(recipeDtoPaginationActual.getObjects(),recipeDtoPagination.getObjects());
        Assert.assertEquals(recipeDtoPaginationActual.getTotalPages(),recipeDtoPagination.getTotalPages());
    }

    @Test
    public void testGetRecipeDetailById(){
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        RecipeDetailDto recipeDetailDto = new RecipeDetailDto();
        recipeDetailDto.setUser(new UserDto());
        RecipeDto recipeDto=new RecipeDto();
        recipeDto.setName(recipe.getName());
        recipeDetailDto.setRecipe(recipeDto);
        when(recipeMapper.toRecipeDetailDto(recipe)).thenReturn(recipeDetailDto);
        FavoriteRecipeEntity favoriteRecipe=new FavoriteRecipeEntity();
        favoriteRecipe.setRecipe(recipe);
        user.setImageData("image".getBytes());
        recipe.setOwner(user);
        when(favoriteRecipeRepository.findRecipesByUser_IdAndRecipe_Id(USER_ID,RECIPE_ID)).thenReturn(favoriteRecipe);
        RecipeDetailDto recipeDetailDtoActual= recipeService.getRecipeDetailById(RECIPE_ID,USER_ID);
        Assert.assertEquals(recipeDetailDtoActual.getRecipe().getName(),recipeDetailDto.getRecipe().getName());
        Assert.assertTrue(recipeDetailDtoActual.getRecipe().isFavorite());
    }

    @Test
    public void testGetRecipeDetailByIdNotFavorite(){
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        RecipeDetailDto recipeDetailDto = new RecipeDetailDto();
        recipeDetailDto.setUser(new UserDto());
        RecipeDto recipeDto=new RecipeDto();
        recipeDto.setName(recipe.getName());
        recipeDetailDto.setRecipe(recipeDto);
        when(recipeMapper.toRecipeDetailDto(recipe)).thenReturn(recipeDetailDto);
        user.setImageData("image".getBytes());
        recipe.setOwner(user);
        when(favoriteRecipeRepository.findRecipesByUser_IdAndRecipe_Id(USER_ID,RECIPE_ID)).thenReturn(null);
        RecipeDetailDto recipeDetailDtoActual= recipeService.getRecipeDetailById(RECIPE_ID,USER_ID);
        Assert.assertEquals(recipeDetailDtoActual.getRecipe().getName(),recipeDetailDto.getRecipe().getName());
        Assert.assertFalse(recipeDetailDtoActual.getRecipe().isFavorite());
    }

    @Test
    public void testGetScheduledRecipesByUserId(){
        List<ScheduleRecipeEntity> recipes = new ArrayList<>();
        ScheduleRecipeEntity recipe1=new ScheduleRecipeEntity();
        recipe1.setNote("note1");
        ScheduleRecipeEntity recipe2=new ScheduleRecipeEntity();
        recipe2.setNote("note1");
        recipes.add(recipe1);
        recipes.add(recipe2);

        List<ScheduleRecipeDto> recipeDtos = new ArrayList<>();
        ScheduleRecipeDto recipeDto1=new ScheduleRecipeDto();
        recipeDto1.setNote("note1");
        ScheduleRecipeDto recipeDto2=new ScheduleRecipeDto();
        recipeDto2.setNote("note1");
        recipeDtos.add(recipeDto1);
        recipeDtos.add(recipeDto2);
        Page<ScheduleRecipeEntity> scheduleRecipePage=new PageImpl<ScheduleRecipeEntity>(recipes);
        when(scheduleRecipeMapper.toDtos(recipes)).thenReturn(recipeDtos);
        when(scheduleRecipeRepository.findScheduleRecipesByUser_IdOrderByScheduleTimeDesc(USER_ID, PageRequest.of(0, 10))).thenReturn(scheduleRecipePage);
        Pagination<ScheduleRecipeDto> scheduleRecipeDtoPaginationActual = recipeService.getScheduledRecipesByUserId(USER_ID,1);
        Assert.assertEquals(scheduleRecipePage.getTotalPages(),scheduleRecipeDtoPaginationActual.getTotalPages());
        Assert.assertEquals(scheduleRecipeDtoPaginationActual.getObjects(),recipeDtos);
    }

    @Test
    public void testScheduleRecipe(){
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        ScheduleRecipeEntity scheduleRecipe=new ScheduleRecipeEntity();
        scheduleRecipe.setUser(user);
        scheduleRecipe.setRecipe(recipe);
        ScheduleRecipeRequest scheduleRecipeRequest = new ScheduleRecipeRequest();
        scheduleRecipeRequest.setUserId(USER_ID);
        scheduleRecipeRequest.setRecipeId(RECIPE_ID);
        scheduleRecipeRequest.setScheduleTime(new Date());
        ArgumentCaptor<ScheduleRecipeEntity> scheduleRecipeEntityArgumentCaptor=ArgumentCaptor.forClass(ScheduleRecipeEntity.class);
        doReturn(scheduleRecipe).when(scheduleRecipeRepository).save(scheduleRecipeEntityArgumentCaptor.capture());
        recipeService.scheduleRecipe(scheduleRecipeRequest);
        Assert.assertEquals(scheduleRecipeEntityArgumentCaptor.getValue().getUser(),user);
        Assert.assertEquals(scheduleRecipeEntityArgumentCaptor.getValue().getRecipe(),recipe);
    }

    @Test
    public void testGetRecipesByUserId(){
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        List<RecipeEntity> recipes = new ArrayList<>();
        RecipeEntity recipe1=new RecipeEntity();
        recipe1.setName("name1");
        RecipeEntity recipe2=new RecipeEntity();
        recipe2.setName("name2");
        recipes.add(recipe1);
        recipes.add(recipe2);
        Page<RecipeEntity> recipePage = new PageImpl<RecipeEntity>(recipes);
        when(recipeRepository.findRecipesByOwner_IdOrderByCreatedDateDesc(USER_ID,PageRequest.of(0, 10))).thenReturn(recipePage);
        List<RecipeDto> recipeDtos=new ArrayList<>();
        RecipeDto recipeDto1=new RecipeDto();
        recipeDto1.setName("name1");
        RecipeDto recipeDto2=new RecipeDto();
        recipeDto2.setName("name2");
        recipeDtos.add(recipeDto1);
        recipeDtos.add(recipeDto2);
        when(recipeMapper.toDtos(recipePage.getContent())).thenReturn(recipeDtos);
        Pagination<RecipeDto> recipeDtoPaginationActual=recipeService.getRecipesByUserId(USER_ID,1);
        Assert.assertEquals(recipeDtoPaginationActual.getTotalPages(),recipePage.getTotalPages());
        Assert.assertEquals(recipeDtoPaginationActual.getObjects(),recipeDtos);
    }

    @Test
    public void testGetLearntRecipesByUserId(){
        LearntRecipeEntity learntRecipe=new LearntRecipeEntity();
        EvaluationEntity evaluation=new EvaluationEntity();
        ImageEntity image=new ImageEntity();
        image.setData("image".getBytes(StandardCharsets.UTF_8));
        evaluation.setImages(Collections.singletonList(image));
        learntRecipe.setEvaluation(evaluation);
        List<LearntRecipeEntity> learntRecipes= Collections.singletonList(learntRecipe);
        Page<LearntRecipeEntity> learntRecipePage=new PageImpl<>(learntRecipes);
        when(learntRecipeRepository.findLearntRecipesByUser_IdOrderByEvaluation_CreatedDateDesc(USER_ID,PageRequest.of(0, 10))).thenReturn(learntRecipePage);

        LearntRecipeDto learntRecipeDto=new LearntRecipeDto();
        EvaluationDto evaluationDto=new EvaluationDto();
        learntRecipeDto.setEvaluation(evaluationDto);
        List<LearntRecipeDto> learntRecipeDtos=Collections.singletonList(learntRecipeDto);
        when(learntRecipeMapper.toDTOs(learntRecipes)).thenReturn(learntRecipeDtos);
        Pagination<LearntRecipeDto> learntRecipeDtoPagination=recipeService.getLearntRecipesByUserId(USER_ID,1);
        Assert.assertEquals(learntRecipeDtoPagination.getObjects(),learntRecipeDtos);
    }
}
