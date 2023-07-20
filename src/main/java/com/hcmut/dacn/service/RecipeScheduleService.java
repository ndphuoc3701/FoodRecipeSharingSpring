//package com.hcmut.dacn.service;
//
//import com.hcmut.dacn.request.RecipeScheduleRequest;
//import com.hcmut.dacn.service.dao.*;
//import com.hcmut.dacn.service.dto.*;
//import com.hcmut.dacn.entity.*;
//import com.hcmut.dacn.service.mapper.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class RecipeScheduleService {
//
////    @Autowired
//    RecipeDao recipeDao;
//
////    @Autowired
//    UserDao userDao;
//
////    @Autowired
//    private RecipeScheduleDao recipeScheduleDao;
//
//    @Autowired
//    private RecipeScheduleMapper recipeScheduleMapper;
//
//    public RecipeScheduleDto create(RecipeScheduleRequest recipeScheduleRequest){
//        UserEntity user=userDao.getByUserId(recipeScheduleRequest.getUserId());
//        RecipeEntity recipe=recipeDao.getByRecipeId(recipeScheduleRequest.getRecipeId());
//        ScheduleRecipeEntity recipeSchedule=new ScheduleRecipeEntity();
//        recipeSchedule.setUser(user);
//        recipeSchedule.setRecipe(recipe);
//        recipeSchedule.setScheduleTime(recipeScheduleRequest.getScheduleTime());
//        recipeSchedule.setNote(recipeScheduleRequest.getNote());
//        return recipeScheduleMapper.toDto(recipeScheduleDao.createOne(recipeSchedule));
//    }
//    public List<RecipeScheduleDto> getAllByUserId(Long userId){
//        return recipeScheduleMapper.toDtos(recipeScheduleDao.getAllByUserId(userId));
//    }
//}
