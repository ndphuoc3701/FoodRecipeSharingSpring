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
import java.util.*;
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
    SimpMessagingTemplate simpMessagingTemplate;
    RestClient restClient = RestClient
            .builder(HttpHost.create("http://localhost:9200"))
            .build();
    ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
    ElasticsearchClient client = new ElasticsearchClient(transport);

    public Pagination<RecipeDto> getAll(Long userId, String keyword, String filter, String ingredient, int page) {
        List<Query> queries = new ArrayList<>();
        if (!keyword.isEmpty()) {
            String[] keywordSplit = keyword.split(" ");
            queries.addAll(Arrays.stream(keywordSplit).map(element -> {
                String unsignedElement = unsignedString(element);
                String ingredientsField = unsignedElement.equalsIgnoreCase(element) ? "unsignedIngredients" : "ingredients";
                return MatchQuery.of(m -> m.field(ingredientsField).query(element))._toQuery();
            }).collect(Collectors.toList()));
        }
        if (!ingredient.isEmpty()) {
            String[] ingredientSplit = ingredient.split(" ");
            List<Query> ingredientQuery = Arrays.stream(ingredientSplit).filter(e->!e.isEmpty()).map(element -> MatchQuery.of(m -> m.field("ingredients")
                    .query(element))._toQuery()).collect(Collectors.toList());
            queries.addAll(ingredientQuery);
        }

        SearchResponse<RecipeDto> searchResponse = null;
        try {
            SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder().index("recipe");
            if (!queries.isEmpty())searchRequestBuilder.query(q -> q.bool(b -> b
                    .must(queries)));

            if (!filter.isEmpty())
                searchRequestBuilder.sort(so -> so.field(f -> f.field(filter).order(SortOrder.Desc)));
//            if (page != 1)
            searchRequestBuilder.from((page - 1) * 12).size(12);
            searchResponse = client.search(searchRequestBuilder.build(), RecipeDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Hit<RecipeDto>> hits = searchResponse.hits().hits();
        List<RecipeDto> recipeDtos = hits.stream().map(Hit::source).collect(Collectors.toList());
        List<Long> favoriteRecipeIds = favoriteRecipeRepository.findRecipesByUser_Id(userId);

        recipeDtos.forEach(r -> {
            if (favoriteRecipeIds.contains(r.getId())) {
                r.setFavorite(true);
            }
        });
        Pagination<RecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages((int)Math.ceil(searchResponse.hits().total().value()/12.0));
        recipeDtoPagination.setObjects(recipeDtos);
        return recipeDtoPagination;
    }

    public List<String> getRecipeByInput(String keyword) {
        List<Query> queries = new ArrayList<>();

        if (!keyword.isEmpty()) {
            String[] keywordSplit = keyword.split(" ");
            String lastWord = keywordSplit[keywordSplit.length - 1];
            String[] keywordSplitExceptLastWord = Arrays.copyOf(keywordSplit, keywordSplit.length - 1);
            queries.addAll(Arrays.stream(keywordSplitExceptLastWord).map(element -> {
                String unsignedElement = unsignedString(element);
                String ingredientsField = unsignedElement.equalsIgnoreCase(element) ? "unsignedName" : "name";
                return MatchQuery.of(m -> m.field(ingredientsField).query(element))._toQuery();
            }).collect(Collectors.toList()));
            String unsignedLastWord = unsignedString(lastWord);
            String ingredientsField = unsignedLastWord.equalsIgnoreCase(lastWord) ? "unsignedName" : "name";
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
        return hits.stream().map(h -> h.source().getName()).collect(Collectors.toList());
    }

    public Pagination<RecipeDto> getRecipesByUserId(Long userId, int page) {
        Page<RecipeEntity> recipeEntityPage = recipeRepository.findRecipesByOwner_IdOrderByCreatedDateDesc(userId, PageRequest.of(page - 1, 10));
        List<RecipeDto> recipeDtos = recipeMapper.toDtos(recipeEntityPage.getContent());
        List<Long> favoriteRecipeIds = favoriteRecipeRepository.findRecipesByUser_Id(userId);
        recipeDtos.forEach(r -> {
            if (favoriteRecipeIds.contains(r.getId())) {
                r.setFavorite(true);
            }
        });
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
        return recipeESRepository.save(recipeDto);
    }

    private String toEsIngredients(String recipeName, List<IngredientRecipeEntity> ingredientRecipeEntities) {
        return ingredientRecipeEntities.stream().map(i -> i.getName() + " ").reduce(recipeName + " ", String::concat);
    }

    public void addFavoriteRecipe(Long userId, Long recipeId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElse(null);
        FavoriteRecipeEntity favoriteRecipe = new FavoriteRecipeEntity();
        favoriteRecipe.setRecipe(recipe);
        favoriteRecipe.setUser(user);
        favoriteRecipe.getRecipe().setNumFavorite(recipe.getNumFavorite() + 1);
        favoriteRecipeRepository.save(favoriteRecipe);
    }

    public void deleteFavoriteRecipe(Long userId, Long recipeId) {
        FavoriteRecipeEntity favoriteRecipe = favoriteRecipeRepository.findRecipesByUser_IdAndRecipe_Id(userId,recipeId);
        favoriteRecipe.getRecipe().setNumFavorite(favoriteRecipe.getRecipe().getNumFavorite()-1);
        favoriteRecipeRepository.delete(favoriteRecipe);
    }

    public void deleteScheduleRecipe(Long userId, Long recipeId){
        ScheduleRecipeEntity scheduleRecipe = scheduleRecipeRepository.findByUser_IdAndRecipe_Id(userId,recipeId);
        scheduleRecipeRepository.delete(scheduleRecipe);
    }

    public Pagination<RecipeDto> getFavoriteRecipesByUserId(Long userId, int page) {
        Page<FavoriteRecipeEntity> favoriteRecipePage = favoriteRecipeRepository.findRecipesByUser_IdOrderByCreatedDateDesc(userId, PageRequest.of(page - 1, 10));
        List<RecipeEntity> recipeEntities=favoriteRecipePage.get().map(FavoriteRecipeEntity::getRecipe).collect(Collectors.toList());

        List<RecipeDto> recipeDtos=recipeMapper.toDtos(recipeEntities);
        recipeDtos.forEach(r->r.setFavorite(true));
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
        List<Long> favoriteRecipeIds = favoriteRecipeRepository.findRecipesByUser_Id(userId);
        learntRecipeDtos.forEach(l -> {
            if (favoriteRecipeIds.contains(l.getRecipe().getId())) {
                l.getRecipe().setFavorite(true);
            }
        });

        Pagination<LearntRecipeDto> recipeDtoPagination = new Pagination<>();
        recipeDtoPagination.setTotalPages(learntRecipePage.getTotalPages());
        recipeDtoPagination.setObjects(learntRecipeDtos);
        return recipeDtoPagination;
    }
    public void updateLearntRecipesByUserId(Long userId, Long recipeId, String note) {
        LearntRecipeEntity learntRecipe=learntRecipeRepository.findByUser_IdAndRecipe_Id(userId,recipeId);
        learntRecipe.getEvaluation().setNote(note);
        learntRecipeRepository.save(learntRecipe);
    }

    public RecipeDetailDto getRecipeDetailById(Long recipeId,Long userId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElse(null);
        RecipeDetailDto recipeDetail = recipeMapper.toRecipeDetailDto(recipe);
        FavoriteRecipeEntity favoriteRecipe = favoriteRecipeRepository.findRecipesByUser_IdAndRecipe_Id(userId,recipeId);
        if(favoriteRecipe!=null)
            recipeDetail.getRecipe().setFavorite(true);
        recipeDetail.getUser().setImage(new String(recipe.getOwner().getImageData(), StandardCharsets.UTF_8));
        return recipeDetail;
    }

    public Pagination<ScheduleRecipeDto> getScheduledRecipesByUserId(boolean old, Long userId, int page) {
        Date currentTime=new Date();
        Page<ScheduleRecipeEntity> scheduleRecipePage = old? scheduleRecipeRepository.findOldByUser_Id(userId, currentTime, PageRequest.of(page - 1, 10)):scheduleRecipeRepository.findByUser_Id(userId, currentTime, PageRequest.of(page - 1, 10));
        List<ScheduleRecipeEntity> scheduleRecipeEntities = scheduleRecipePage.getContent();
        List<ScheduleRecipeDto> scheduleRecipeDtos = scheduleRecipeMapper.toDtos(scheduleRecipeEntities);
        List<Long> favoriteRecipeIds = favoriteRecipeRepository.findRecipesByUser_Id(userId);
        scheduleRecipeDtos.forEach(r -> {
            if (favoriteRecipeIds.contains(r.getRecipe().getId())) {
                r.getRecipe().setFavorite(true);
            }
        });
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
        scheduleTimer(scheduleRecipe.getRecipe(),scheduleRecipe.getScheduleTime(),scheduleRecipe.getUser().getId());
//        ScheduleRecipeTimerDto scheduleRecipeTimerDto = new ScheduleRecipeTimerDto(recipe.getName(),new String(recipe.getImageData(),StandardCharsets.UTF_8),scheduleRecipe.getRecipe().getId(),scheduleRecipeRequest.getScheduleTime());
//        new Timer().schedule(new ScheduleRecipeTimer(scheduleRecipeTimerDto, scheduleRecipeRequest.getUserId(), simpMessagingTemplate,scheduleRecipeRepository), scheduleRecipeRequest.getScheduleTime());
    }

    public void updateScheduleRecipe(ScheduleRecipeRequest scheduleRecipeRequest) {
        ScheduleRecipeEntity scheduleRecipe = scheduleRecipeRepository.findByUser_IdAndRecipe_Id(scheduleRecipeRequest.getUserId(),scheduleRecipeRequest.getRecipeId());
        scheduleRecipe.setScheduleTime(scheduleRecipeRequest.getScheduleTime());
        scheduleRecipe.setNote(scheduleRecipeRequest.getNote());
        scheduleRecipeRepository.save(scheduleRecipe);
        scheduleTimer(scheduleRecipe.getRecipe(),scheduleRecipe.getScheduleTime(),scheduleRecipe.getUser().getId());
//        ScheduleRecipeTimerDto scheduleRecipeTimerDto = new ScheduleRecipeTimerDto(scheduleRecipe.getRecipe().getName(),new String(scheduleRecipe.getRecipe().getImageData(),StandardCharsets.UTF_8),scheduleRecipe.getRecipe().getId(),scheduleRecipeRequest.getScheduleTime());
//        new Timer().schedule(new ScheduleRecipeTimer(scheduleRecipeTimerDto, scheduleRecipeRequest.getUserId(), simpMessagingTemplate,scheduleRecipeRepository), scheduleRecipeRequest.getScheduleTime());
    }

    private void scheduleTimer(RecipeEntity recipe, Date time, Long userId){
        ScheduleRecipeTimerDto scheduleRecipeTimerDto = new ScheduleRecipeTimerDto(recipe.getName(),new String(recipe.getImageData(),StandardCharsets.UTF_8),recipe.getId(),time);
        new Timer().schedule(new ScheduleRecipeTimer(scheduleRecipeTimerDto, userId, simpMessagingTemplate,scheduleRecipeRepository), time);
    }

    private String unsignedString(String signString) {
        String nfdNormalizedString = Normalizer.normalize(signString, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replace("đ", "d");
    }
//
//    public List<RecipeDto> createList(List<RecipeSharingDto> recipeRequests) {
//        return recipeRequests.stream().map(this::create).collect(Collectors.toList());
//    }


}
