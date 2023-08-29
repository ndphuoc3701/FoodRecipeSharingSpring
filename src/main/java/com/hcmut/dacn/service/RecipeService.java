package com.hcmut.dacn.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.hcmut.dacn.dto.*;
import com.hcmut.dacn.esRepo.RecipeESRepository;
import com.hcmut.dacn.mapper.LearntRecipeMapper;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.mapper.ScheduleRecipeMapper;
import com.hcmut.dacn.repository.*;
import com.hcmut.dacn.request.ScheduleRecipeRequest;
import com.hcmut.dacn.request.ScheduleRecipeTimer;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.hcmut.dacn.entity.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
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
    RestClient restClient = RestClient
            .builder(HttpHost.create("http://localhost:9200"))
            .build();
    ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
    ElasticsearchClient client = new ElasticsearchClient(transport);

    public Pagination<RecipeDto> getAll(String keyword, String filter, String ingredient, int page) {
        Page<RecipeDto> recipeDtoPage;
        List<Query> queries = null;
        if (!keyword.isEmpty()) {
            String[] keywordSplit = keyword.split(" ");
            queries = Arrays.stream(keywordSplit).map(element -> {
                String unsignedElement = unsignedString(element);
                String ingredientsField = unsignedElement.equalsIgnoreCase(element) ? "unsignedIngredients" : "ingredients";
                return MatchQuery.of(m -> m.field(ingredientsField).query(element))._toQuery();
            }).collect(Collectors.toList());
        }
        if (!ingredient.isEmpty()) {
            String[] ingredientSplit = ingredient.split(" ");
            List<Query> ingredientQuery = Arrays.stream(ingredientSplit).map(element -> MatchQuery.of(m -> m.field("ingredients")
                    .query(element))._toQuery()).collect(Collectors.toList());
            if (queries == null) {
                queries = ingredientQuery;
            } else queries.addAll(ingredientQuery);
        }

        SearchResponse<RecipeDto> searchResponse = null;
        try {
            List<Query> finalQueries = queries;
            SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder().index("recipe")
                    .query(q -> q.bool(b -> b
                            .must(finalQueries)));
            if (!filter.isEmpty())
                searchRequestBuilder.sort(so -> so.field(f -> f.field(filter).order(SortOrder.Desc)));
            if (page != 1)
                searchRequestBuilder.from((page - 1) * 12).size(12);
            searchResponse = client.search(searchRequestBuilder.build(), RecipeDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Hit<RecipeDto>> hits = searchResponse.hits().hits();
        List<RecipeDto> recipeDtos = hits.stream().map(Hit::source).collect(Collectors.toList());

        Pagination<RecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages((int) searchResponse.hits().total().value() / 12 + 1);
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }

    public List<String> getRecipeByInput(String keyword) {
        List<Query> queries = null;
        if (!keyword.isEmpty()) {
            String[] keywordSplit = keyword.split(" ");
            String lastWord = keywordSplit[keywordSplit.length - 1];
            String[] keywordSplitExceptLastWord = Arrays.copyOf(keywordSplit, keywordSplit.length - 1);
            queries = Arrays.stream(keywordSplitExceptLastWord).map(element -> {
                String unsignedElement = unsignedString(element);
                String ingredientsField = unsignedElement.equalsIgnoreCase(element) ? "unsignedIngredients" : "ingredients";
                return MatchQuery.of(m -> m.field(ingredientsField).query(element))._toQuery();
            }).collect(Collectors.toList());
            String unsignedLastWord = unsignedString(lastWord);
            String ingredientsField = unsignedLastWord.equalsIgnoreCase(lastWord) ? "unsignedIngredients" : "ingredients";
            String wordQuery = unsignedLastWord.equalsIgnoreCase(lastWord) ? unsignedLastWord : lastWord;
            Query lastWordQuery = MatchQuery.of(m -> m.field(ingredientsField).fuzziness("2").prefixLength(unsignedLastWord.length()).query(wordQuery))._toQuery();
            queries.add(lastWordQuery);
        }

        SearchResponse<RecipeDto> searchResponse = null;
        try {
            List<Query> finalQueries = queries;
            SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder().index("recipe")
                    .query(q -> q.bool(b -> b
                            .must(finalQueries))).size(10);
            searchResponse = client.search(searchRequestBuilder.build(), RecipeDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Hit<RecipeDto>> hits = searchResponse.hits().hits();
        List<String> recipeDtos = hits.stream().map(h -> h.source().getName()).collect(Collectors.toList());
        return recipeDtos;
    }

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
        RecipeDto recipeDto = recipeMapper.toEsDto(recipeRepository.save(recipe));
        String esIngredients = toEsIngredients(recipe.getName(), ingredientRecipes);
        recipeDto.setIngredients(esIngredients);
        recipeDto.setUnsignedIngredients(unsignedString(esIngredients));
        recipeDto.setUnsignedName(unsignedString(recipe.getName()));
//        return recipeESRepository.save(recipeDto);
        return null;
    }

    public String toEsIngredients(String recipeName, List<IngredientRecipeEntity> ingredientRecipeEntities) {
        return ingredientRecipeEntities.stream().map(i -> i.getName() + " ").reduce(recipeName + " ", String::concat);
    }

    public void addFavoriteRecipe(Long userId, Long recipeId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElse(null);
//        RecipeDto recipeDto = recipeESRepository.findById(recipeId).orElse(null);
        FavoriteRecipeEntity favoriteRecipe = new FavoriteRecipeEntity();
        RecipeDto recipeDto = new RecipeDto();
        favoriteRecipe.setRecipe(recipe);
        favoriteRecipe.setUser(user);
        favoriteRecipeRepository.save(favoriteRecipe);
        Integer newNumFavorite = recipe.getNumFavorite() + 1;
        recipe.setNumFavorite(newNumFavorite);
        recipeRepository.save(recipe);
        recipeDto.setNumFavorite(newNumFavorite);
//        recipeESRepository.save(recipeDto);
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

    public void scheduleRecipe(ScheduleRecipeRequest scheduleRecipeRequest) {
        UserEntity user = userRepository.findById(scheduleRecipeRequest.getUserId()).orElse(null);
        RecipeEntity recipe = recipeRepository.findById(scheduleRecipeRequest.getRecipeId()).orElse(null);
        ScheduleRecipeEntity scheduleRecipe = new ScheduleRecipeEntity();
        scheduleRecipe.setUser(user);
        scheduleRecipe.setRecipe(recipe);
        scheduleRecipe.setScheduleTime(scheduleRecipeRequest.getScheduleTime());
        scheduleRecipe.setNote(scheduleRecipeRequest.getNote());
        scheduleRecipeRepository.save(scheduleRecipe);
        ScheduleRecipeTimerDto scheduleRecipeTimerDto = new ScheduleRecipeTimerDto(recipe.getName(),new String(recipe.getImageData(),StandardCharsets.UTF_8),scheduleRecipeRequest.getScheduleTime());
        new Timer().schedule(new ScheduleRecipeTimer(scheduleRecipeTimerDto, scheduleRecipeRequest.getUserId(), simpMessagingTemplate), scheduleRecipeRequest.getScheduleTime());
    }

    private String unsignedString(String signString) {
        String nfdNormalizedString = Normalizer.normalize(signString, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replace("đ", "d");
    }

    public List<RecipeDto> createList(List<RecipeSharingDto> recipeRequests) {
        return recipeRequests.stream().map(this::create).collect(Collectors.toList());
    }

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

//    public void sendNotification(Schedule schedule) {
////        String address = "/queue/"+schedule.getUserId()+"/notification";
////        simpMessagingTemplate.convertAndSend(address, schedule.getContent());
//        new Timer().schedule(new ScheduleTest(schedule.getContent(), schedule.getUserId(), simpMessagingTemplate), schedule.getScheduleTime());
//    }
}
