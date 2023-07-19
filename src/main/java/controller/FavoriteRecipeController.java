package controller;

import com.hcmut.dacn.request.FavoriteRecipeRequest;
import com.hcmut.dacn.service.FavoriteRecipeService;
import com.hcmut.dacn.service.dto.FavoriteRecipeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("favorite-recipes")
public class FavoriteRecipeController {
    public final static String PATH="favorite-recipes";
    @Autowired
    FavoriteRecipeService favoriteRecipeService;

    @GetMapping("{userId}")
    public List<FavoriteRecipeDto> getByUserId(@PathVariable("userId") Long userId){
        return favoriteRecipeService.getByUserId(userId);
    }

    @PostMapping
    public FavoriteRecipeDto create(FavoriteRecipeRequest favoriteRecipeRequest){
        return favoriteRecipeService.create(favoriteRecipeRequest);
    }
}
