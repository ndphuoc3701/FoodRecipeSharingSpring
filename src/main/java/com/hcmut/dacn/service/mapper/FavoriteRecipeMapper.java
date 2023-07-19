package com.hcmut.dacn.service.mapper;

import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteRecipeMapper {
    @Mapping(target = "recipe.material",ignore = true)
    @Mapping(target = "recipe.instruction",ignore = true)
    FavoriteRecipeDto toDTO(FavoriteRecipeEntity favoriteRecipe);
    List<FavoriteRecipeDto> toDTOs(List<FavoriteRecipeEntity> favoriteRecipes);
}

//@Mapper(componentModel = "cdi")
//public abstract class FavoriteRecipeMapper{
//    @BeforeMapping
//    public void removeFavoriteRecipesDtoOfOwnerDtoOfFavoriteRecipe(FavoriteRecipeEntity favoriteRecipe) {
//        favoriteRecipe.getRecipe().getOwner().setFavoriteRecipes(null);
//    }
//    public abstract FavoriteRecipeDTO toDTO(FavoriteRecipeEntity favoriteRecipe);
//    public abstract List<FavoriteRecipeDTO> toDTOs(List<FavoriteRecipeEntity> favoriteRecipes);
//}