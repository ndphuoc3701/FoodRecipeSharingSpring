//package com.hcmut.dacn.service;
//
//import com.hcmut.dacn.entity.FavoriteRecipeEntity;
//import com.hcmut.dacn.request.FavoriteRecipeRequest;
//import com.hcmut.dacn.service.dao.*;
//import com.hcmut.dacn.service.dto.*;
//
//import com.hcmut.dacn.service.mapper.*;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class FavoriteRecipeService {
//
////    @Autowired
//    private FavoriteRecipeDao favoriteRecipeDao;
//
//    @Autowired
//    private LearntRecipeMapper learntRecipeMapper;
//
////    @Autowired
//    private UserDao userDao;
//
////    @Autowired
//    private RecipeDao recipeDao;
//    public List<LearntRecipeDto> getByUserId(Long userId){
//        System.out.println("before service");
//        return learntRecipeMapper.toDTOs(favoriteRecipeDao.getAllByUserId(userId));
//    }
//    public FavoriteRecipeDto create(FavoriteRecipeRequest favoriteRecipeRequest){
//        return learntRecipeMapper.toDTO(favoriteRecipeDao.createOne(new FavoriteRecipeEntity(
//                userDao.getByUserId(favoriteRecipeRequest.getUserId()),
//                recipeDao.getByRecipeId(favoriteRecipeRequest.getRecipeId()),
//                favoriteRecipeRequest.getNote())));
//    }
//}
