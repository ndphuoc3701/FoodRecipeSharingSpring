//package com.hcmut.dacn.service.mapper;
//
//import com.hcmut.dacn.entity.*;
//import org.mapstruct.*;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface LearntRecipeMapper {
//    @Mapping(target = "recipe.ingredients",ignore = true)
//    @Mapping(target = "recipe.instructions",ignore = true)
//    FavoriteRecipeDto toDTO(FavoriteRecipeEntity favoriteRecipe);
//    List<FavoriteRecipeDto> toDTOs(List<FavoriteRecipeEntity> favoriteRecipes);
//}
//
////@Mapper(componentModel = "cdi")
////public abstract class FavoriteRecipeMapper{
////    @BeforeMapping
////    public void removeFavoriteRecipesDtoOfOwnerDtoOfFavoriteRecipe(FavoriteRecipeEntity favoriteRecipe) {
////        favoriteRecipe.getRecipe().getOwner().setFavoriteRecipes(null);
////    }
////    public abstract FavoriteRecipeDTO toDTO(FavoriteRecipeEntity favoriteRecipe);
////    public abstract List<FavoriteRecipeDTO> toDTOs(List<FavoriteRecipeEntity> favoriteRecipes);
////}