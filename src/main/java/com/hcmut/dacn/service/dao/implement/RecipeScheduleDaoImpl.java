//package com.hcmut.dacn.service.dao.implement;
//
//import com.axonactive.entity.RecipeScheduleEntity;
//import com.axonactive.service.dao.RecipeScheduleDao;
//
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import java.util.List;
//
//@Stateless
//public class RecipeScheduleDaoImpl implements RecipeScheduleDao {
//    @PersistenceContext(name = "FoodRecipe")
//    EntityManager em;
//    @Override
//    public RecipeScheduleEntity createOne(RecipeScheduleEntity recipeSchedule) {
//        return em.merge(recipeSchedule);
//    }
//
////    @Override
////    public RecipeScheduleEntity getByRecipeScheduleId(Long recipeScheduleId) {
////        Query getByRecipeScheduleIdQuery=em.createQuery("SELECT r FROM RecipeScheduleEntity r WHERE " +
////                "r.id=:recipeScheduleId",RecipeScheduleEntity.class);
////        getByRecipeScheduleIdQuery.setParameter("recipeScheduleId",recipeScheduleId);
////        return (RecipeScheduleEntity) getByRecipeScheduleIdQuery.getSingleResult();
////    }
//
//    @Override
//    public List<RecipeScheduleEntity> getAllByUserId(Long userId) {
//        Query getAllByUserIdQuery=em.createQuery("SELECT r FROM RecipeScheduleEntity r WHERE " +
//                "r.user.id=:userId",RecipeScheduleEntity.class);
//        getAllByUserIdQuery.setParameter("userId",userId);
//        return getAllByUserIdQuery.getResultList();
//    }
//
//}
