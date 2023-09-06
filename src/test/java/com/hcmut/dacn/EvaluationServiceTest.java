package com.hcmut.dacn;

import com.hcmut.dacn.dto.EvaluationDto;
import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.dto.Pagination;
import com.hcmut.dacn.entity.EvaluationEntity;
import com.hcmut.dacn.entity.RecipeEntity;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.esRepo.RecipeESRepository;
import com.hcmut.dacn.mapper.EvaluationMapper;
import com.hcmut.dacn.repository.EvaluationRepository;
import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.EvaluationRequest;
import com.hcmut.dacn.service.EvaluationService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationServiceTest {
    @MockBean
    private EvaluationRepository evaluationRepository;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private RecipeESRepository recipeESRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EvaluationMapper evaluationMapper;
    @Autowired
    EvaluationService evaluationService;
    private final ArgumentCaptor<EvaluationEntity> evaluationArgumentCaptor=ArgumentCaptor.forClass(EvaluationEntity.class);


    private static final String NAME_RECIPE="Bun Bo";
    private static final String IMAGE_RECIPE="image";
    private static final Long RECIPE_ID=1L;
    private static final Long USER_ID=1L;
    private static final Long EVALUATION_ID=1L;
    private static RecipeEntity recipe;
    private static UserEntity user;

    @BeforeClass
    public static void initData(){
        user = new UserEntity();
        recipe = new RecipeEntity();
        recipe.setName(NAME_RECIPE);
        recipe.setImageData(IMAGE_RECIPE.getBytes(StandardCharsets.UTF_8));
        recipe.setOwner(user);
        recipe.setInstructions(new ArrayList<>());
        recipe.setIngredientRecipes(new ArrayList<>());
    }
    @Test
    public void testGetEvaluationsByRecipeId(){
        List<EvaluationEntity> evaluations=new ArrayList<>();
        EvaluationEntity evaluation1=new EvaluationEntity();
        EvaluationEntity evaluation2=new EvaluationEntity();
        evaluations.add(evaluation1);
        evaluations.add(evaluation2);
        Page<EvaluationEntity> evaluationPage=new PageImpl<>(evaluations);
        when(evaluationRepository.findEvaluationsByRecipe_IdOrderByCreatedDateDesc(RECIPE_ID, PageRequest.of(0, 5))).thenReturn(evaluationPage);

        List<EvaluationDto> evaluationDtos=new ArrayList<>();
        EvaluationDto evaluationDto1=new EvaluationDto();
        EvaluationDto evaluationDto2=new EvaluationDto();
        evaluationDtos.add(evaluationDto1);
        evaluationDtos.add(evaluationDto2);
        when(evaluationMapper.toDtos(evaluations)).thenReturn(evaluationDtos);

        Pagination<EvaluationDto> evaluationDtoPaginationExpect= evaluationService.getEvaluationsByRecipeId(RECIPE_ID,1);
        Assert.assertEquals(evaluationDtoPaginationExpect.getTotalPages(),evaluationPage.getTotalPages());
        Assert.assertEquals(evaluationDtoPaginationExpect.getObjects(),evaluationDtos);
    }

    @Test
    public void testCreateEvaluation(){
        UserEntity evaluateUser = new UserEntity();
        evaluateUser.setNumLike(0);
        evaluateUser.setEvaluationLevel(0.0);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(evaluateUser));
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        recipe.setNumEvaluation(0);
        recipe.setNumStar(0.0);
        recipe.setNumEvaluation(0);
        recipe.setNumLike(0);
        user.setCookLevel(0.0);
        user.setId(USER_ID);
        EvaluationRequest evaluationRequest=new EvaluationRequest();
        evaluationRequest.setNumStar(5);
        evaluationRequest.setUserId(USER_ID);
        evaluationRequest.setRecipeId(RECIPE_ID);
        ImageDto imageDto=new ImageDto();
        imageDto.setData("image");
        evaluationRequest.setImages(Arrays.asList(imageDto));
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);
        RecipeEntity recipeExpect=new RecipeEntity();
        recipeExpect.setOwner(new UserEntity());
        recipeExpect.getOwner().setCookLevel(5.0);
        recipeExpect.setNumEvaluation(1);
        recipeExpect.setNumStar(5.0);
        recipeExpect.setNumLike(1);
        doReturn(new EvaluationEntity()).when(evaluationRepository).save(evaluationArgumentCaptor.capture());

        evaluationService.create(evaluationRequest);
        Assert.assertEquals(evaluationArgumentCaptor.getValue().getNumStar(),evaluationRequest.getNumStar());
        UserEntity evaluateUserActual=evaluationArgumentCaptor.getValue().getUser();
        Assert.assertEquals(evaluateUserActual.getEvaluationLevel(),Double.valueOf(0.05));
        Assert.assertEquals(evaluateUserActual.getNumLike(),Integer.valueOf(5));
        RecipeEntity recipeActual=evaluationArgumentCaptor.getValue().getRecipe();
        Assert.assertEquals(recipeActual.getOwner().getCookLevel(),recipeExpect.getOwner().getCookLevel());
        Assert.assertEquals(recipeActual.getNumStar(),recipeExpect.getNumStar());
        Assert.assertEquals(recipeActual.getNumEvaluation(), recipeExpect.getNumEvaluation());
        Assert.assertEquals(recipeActual.getNumLike(), recipeExpect.getNumLike());
    }

    @Test
    public void testCreateEvaluation2(){
        UserEntity evaluateUser = new UserEntity();
        evaluateUser.setNumLike(5);
        evaluateUser.setEvaluationLevel(0.05);
        when(userRepository.findById(2L)).thenReturn(Optional.of(evaluateUser));
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.ofNullable(recipe));
        recipe.setNumEvaluation(1);
        recipe.setNumStar(5.0);
        recipe.setNumLike(1);
        user.setCookLevel(5.0);
        user.setId(USER_ID);
        EvaluationRequest evaluationRequest=new EvaluationRequest();
        evaluationRequest.setNumStar(1);
        evaluationRequest.setUserId(2L);
        evaluationRequest.setRecipeId(RECIPE_ID);
        evaluationRequest.setImages(new ArrayList<>());
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);

        RecipeEntity recipeExpect=new RecipeEntity();
        recipeExpect.setOwner(new UserEntity());
        recipeExpect.getOwner().setCookLevel(3.0);
        recipeExpect.setNumEvaluation(2);
        recipeExpect.setNumStar(3.0);
        recipeExpect.setNumLike(2);
        doReturn(new EvaluationEntity()).when(evaluationRepository).save(evaluationArgumentCaptor.capture());

        evaluationService.create(evaluationRequest);
        Assert.assertEquals(evaluationArgumentCaptor.getValue().getNumStar(),evaluationRequest.getNumStar());
        UserEntity evaluateUserActual=evaluationArgumentCaptor.getValue().getUser();
        Assert.assertEquals(evaluateUserActual.getEvaluationLevel(),Double.valueOf(0.1));
        Assert.assertEquals(evaluateUserActual.getNumLike(),Integer.valueOf(10));
        RecipeEntity recipeActual=evaluationArgumentCaptor.getValue().getRecipe();
        Assert.assertEquals(recipeActual.getOwner().getCookLevel(),recipeExpect.getOwner().getCookLevel());
        Assert.assertEquals(recipeActual.getNumStar(),recipeExpect.getNumStar());
        Assert.assertEquals(recipeActual.getNumEvaluation(), recipeExpect.getNumEvaluation());
        Assert.assertEquals(recipeActual.getNumLike(), recipeExpect.getNumLike());
    }

    @Test
    public void testLikeEvaluation(){
        EvaluationEntity evaluation=new EvaluationEntity();
        when(evaluationRepository.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        evaluation.setNumLike(0);
        evaluation.setNumDislike(0);
        evaluation.setRecipe(recipe);
        evaluation.setNumStar(2);
        recipe.setNumLike(2);
        recipe.setNumStar(3.5);
        user.setId(USER_ID);
        user.setCookLevel(3.5);
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);
        UserEntity evaluateUser=new UserEntity();
        evaluation.setUser(evaluateUser);
        evaluateUser.setNumLike(5);
        evaluateUser.setEvaluationLevel(0.05);
        evaluation.setUser(evaluateUser);

        EvaluationEntity evaluationExpect=new EvaluationEntity();
        evaluationExpect.setNumLike(2);
        RecipeEntity evaluationExpectRecipe=new RecipeEntity();
        evaluationExpect.setRecipe(evaluationExpectRecipe);
        evaluationExpectRecipe.setNumLike(3);
        evaluationExpectRecipe.setNumStar(3.0);
        UserEntity evaluationExpectRecipeOwner=new UserEntity();
        evaluationExpectRecipeOwner.setCookLevel(3.0);
        evaluationExpectRecipe.setOwner(evaluationExpectRecipeOwner);
        UserEntity evaluationExpectUser=new UserEntity();
        evaluationExpect.setUser(evaluationExpectUser);
        evaluationExpectUser.setNumLike(6);
        evaluationExpectUser.setEvaluationLevel(0.06);
        doReturn(evaluationExpect).when(evaluationRepository).save(evaluationArgumentCaptor.capture());
        evaluationService.likeOrDislikeEvaluation(EVALUATION_ID,true);
        EvaluationEntity evaluationActual = evaluationArgumentCaptor.getValue();
        Assert.assertEquals(evaluationActual.getUser().getNumLike(),evaluationExpect.getUser().getNumLike());
        Assert.assertEquals(evaluationActual.getUser().getEvaluationLevel(),evaluationExpect.getUser().getEvaluationLevel());
        Assert.assertEquals(evaluationActual.getRecipe().getNumLike(),evaluationExpect.getRecipe().getNumLike());
        Assert.assertEquals(evaluationActual.getRecipe().getNumStar(),evaluationExpect.getRecipe().getNumStar());
        Assert.assertEquals(evaluationActual.getRecipe().getOwner().getCookLevel(),evaluationExpect.getRecipe().getOwner().getCookLevel());
    }

    @Test
    public void testLikeEvaluation2(){
        EvaluationEntity evaluation=new EvaluationEntity();
        when(evaluationRepository.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        evaluation.setNumLike(0);
        evaluation.setNumDislike(1);
        evaluation.setRecipe(recipe);
        evaluation.setNumStar(2);
        recipe.setNumLike(1);
        recipe.setNumStar(5.0);
        user.setId(USER_ID);
        user.setCookLevel(5.0);
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);
        UserEntity evaluateUser=new UserEntity();
        evaluation.setUser(evaluateUser);
        evaluateUser.setNumLike(0);
        evaluateUser.setEvaluationLevel(0.00);
        evaluation.setUser(evaluateUser);

        EvaluationEntity evaluationExpect=new EvaluationEntity();
        evaluationExpect.setNumLike(1);
        RecipeEntity evaluationExpectRecipe=new RecipeEntity();
        evaluationExpect.setRecipe(evaluationExpectRecipe);
        evaluationExpectRecipe.setNumLike(2);
        evaluationExpectRecipe.setNumStar(3.5);
        UserEntity evaluationExpectRecipeOwner=new UserEntity();
        evaluationExpectRecipeOwner.setCookLevel(3.5);
        evaluationExpectRecipe.setOwner(evaluationExpectRecipeOwner);
        UserEntity evaluationExpectUser=new UserEntity();
        evaluationExpect.setUser(evaluationExpectUser);
        evaluationExpectUser.setNumLike(5);
        evaluationExpectUser.setEvaluationLevel(0.05);
        doReturn(evaluationExpect).when(evaluationRepository).save(evaluationArgumentCaptor.capture());
        evaluationService.likeOrDislikeEvaluation(EVALUATION_ID,true);
        EvaluationEntity evaluationActual = evaluationArgumentCaptor.getValue();
        Assert.assertEquals(evaluationActual.getUser().getNumLike(),evaluationExpect.getUser().getNumLike());
        Assert.assertEquals(evaluationActual.getUser().getEvaluationLevel(),evaluationExpect.getUser().getEvaluationLevel());
        Assert.assertEquals(evaluationActual.getRecipe().getNumLike(),evaluationExpect.getRecipe().getNumLike());
        Assert.assertEquals(evaluationActual.getRecipe().getNumStar(),evaluationExpect.getRecipe().getNumStar());
        Assert.assertEquals(evaluationActual.getRecipe().getOwner().getCookLevel(),evaluationExpect.getRecipe().getOwner().getCookLevel());
    }

    @Test
    public void testDislikeEvaluation(){
        EvaluationEntity evaluation=new EvaluationEntity();
        when(evaluationRepository.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        evaluation.setNumLike(1);
        evaluation.setNumDislike(0);
        evaluation.setRecipe(recipe);
        evaluation.setNumStar(2);
        recipe.setNumLike(3);
        recipe.setNumStar(3.0);
        user.setId(USER_ID);
        user.setCookLevel(3.0);
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);
        UserEntity evaluateUser=new UserEntity();
        evaluation.setUser(evaluateUser);
        evaluateUser.setNumLike(6);
        evaluateUser.setEvaluationLevel(0.06);
        evaluation.setUser(evaluateUser);

        EvaluationEntity evaluationExpect=new EvaluationEntity();
        evaluationExpect.setNumLike(1);
        evaluationExpect.setNumDislike(1);
        RecipeEntity evaluationExpectRecipe=new RecipeEntity();
        evaluationExpect.setRecipe(evaluationExpectRecipe);
        evaluationExpectRecipe.setNumLike(2);
        evaluationExpectRecipe.setNumStar(3.5);
        UserEntity evaluationExpectRecipeOwner=new UserEntity();
        evaluationExpectRecipeOwner.setCookLevel(3.5);
        evaluationExpectRecipe.setOwner(evaluationExpectRecipeOwner);
        UserEntity evaluationExpectUser=new UserEntity();
        evaluationExpect.setUser(evaluationExpectUser);
        evaluationExpectUser.setNumLike(5);
        evaluationExpectUser.setEvaluationLevel(0.05);
        doReturn(evaluationExpect).when(evaluationRepository).save(evaluationArgumentCaptor.capture());
        evaluationService.likeOrDislikeEvaluation(EVALUATION_ID,false);
        EvaluationEntity evaluationActual = evaluationArgumentCaptor.getValue();
        Assert.assertEquals(evaluationActual.getUser().getNumLike(),evaluationExpect.getUser().getNumLike());
        Assert.assertEquals(evaluationActual.getUser().getEvaluationLevel(),evaluationExpect.getUser().getEvaluationLevel());
        Assert.assertEquals(evaluationActual.getRecipe().getNumLike(),evaluationExpect.getRecipe().getNumLike());
        Assert.assertEquals(evaluationActual.getRecipe().getNumStar(),evaluationExpect.getRecipe().getNumStar());
        Assert.assertEquals(evaluationActual.getRecipe().getOwner().getCookLevel(),evaluationExpect.getRecipe().getOwner().getCookLevel());
    }

    @Test
    public void testDislikeEvaluation2(){
        EvaluationEntity evaluation=new EvaluationEntity();
        when(evaluationRepository.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        evaluation.setNumLike(0);
        evaluation.setNumDislike(0);
        evaluation.setRecipe(recipe);
        evaluation.setNumStar(2);
        recipe.setNumLike(2);
        recipe.setNumStar(3.5);
        user.setId(USER_ID);
        user.setCookLevel(3.5);
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);
        UserEntity evaluateUser=new UserEntity();
        evaluation.setUser(evaluateUser);
        evaluateUser.setNumLike(5);
        evaluateUser.setEvaluationLevel(0.05);
        evaluation.setUser(evaluateUser);

        EvaluationEntity evaluationExpect=new EvaluationEntity();
        evaluationExpect.setNumDislike(1);
        RecipeEntity evaluationExpectRecipe=new RecipeEntity();
        evaluationExpect.setRecipe(evaluationExpectRecipe);
        evaluationExpectRecipe.setNumLike(1);
        evaluationExpectRecipe.setNumStar(5.0);
        UserEntity evaluationExpectRecipeOwner=new UserEntity();
        evaluationExpectRecipeOwner.setCookLevel(5.0);
        evaluationExpectRecipe.setOwner(evaluationExpectRecipeOwner);
        UserEntity evaluationExpectUser=new UserEntity();
        evaluationExpect.setUser(evaluationExpectUser);
        evaluationExpectUser.setNumLike(0);
        evaluationExpectUser.setEvaluationLevel(0.0);
        doReturn(evaluationExpect).when(evaluationRepository).save(evaluationArgumentCaptor.capture());
        evaluationService.likeOrDislikeEvaluation(EVALUATION_ID,false);
        EvaluationEntity evaluationActual = evaluationArgumentCaptor.getValue();
        Assert.assertEquals(evaluationActual.getUser().getNumLike(),evaluationExpect.getUser().getNumLike());
        Assert.assertEquals(evaluationActual.getUser().getEvaluationLevel(),evaluationExpect.getUser().getEvaluationLevel());
        Assert.assertEquals(evaluationActual.getRecipe().getNumLike(),evaluationExpect.getRecipe().getNumLike());
        Assert.assertEquals(evaluationActual.getRecipe().getNumStar(),evaluationExpect.getRecipe().getNumStar());
        Assert.assertEquals(evaluationActual.getRecipe().getOwner().getCookLevel(),evaluationExpect.getRecipe().getOwner().getCookLevel());
    }

    @Test
    public void testDislikeEvaluation3(){
        EvaluationEntity evaluation=new EvaluationEntity();
        when(evaluationRepository.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        evaluation.setNumLike(0);
        evaluation.setNumDislike(0);
        evaluation.setRecipe(recipe);
        evaluation.setNumStar(2);
        recipe.setNumLike(1);
        recipe.setNumStar(2.0);
        user.setId(USER_ID);
        user.setCookLevel(2.0);
        when(recipeRepository.numberRecipeOfUserId(USER_ID)).thenReturn(1);
        UserEntity evaluateUser=new UserEntity();
        evaluation.setUser(evaluateUser);
        evaluateUser.setNumLike(5);
        evaluateUser.setEvaluationLevel(0.05);
        evaluation.setUser(evaluateUser);

        EvaluationEntity evaluationExpect=new EvaluationEntity();
        evaluationExpect.setNumDislike(1);
        RecipeEntity evaluationExpectRecipe=new RecipeEntity();
        evaluationExpect.setRecipe(evaluationExpectRecipe);
        evaluationExpectRecipe.setNumLike(0);
        evaluationExpectRecipe.setNumStar(0.0);
        UserEntity evaluationExpectRecipeOwner=new UserEntity();
        evaluationExpectRecipeOwner.setCookLevel(0.0);
        evaluationExpectRecipe.setOwner(evaluationExpectRecipeOwner);
        UserEntity evaluationExpectUser=new UserEntity();
        evaluationExpect.setUser(evaluationExpectUser);
        evaluationExpectUser.setNumLike(0);
        evaluationExpectUser.setEvaluationLevel(0.0);
        doReturn(evaluationExpect).when(evaluationRepository).save(evaluationArgumentCaptor.capture());
        evaluationService.likeOrDislikeEvaluation(EVALUATION_ID,false);
        EvaluationEntity evaluationActual = evaluationArgumentCaptor.getValue();
        Assert.assertEquals(evaluationActual.getUser().getNumLike(),evaluationExpect.getUser().getNumLike());
        Assert.assertEquals(evaluationActual.getUser().getEvaluationLevel(),evaluationExpect.getUser().getEvaluationLevel());
        Assert.assertEquals(evaluationActual.getRecipe().getNumLike(),evaluationExpect.getRecipe().getNumLike());
        Assert.assertEquals(evaluationActual.getRecipe().getNumStar(),evaluationExpect.getRecipe().getNumStar());
        Assert.assertEquals(evaluationActual.getRecipe().getOwner().getCookLevel(),evaluationExpect.getRecipe().getOwner().getCookLevel());
    }
}
