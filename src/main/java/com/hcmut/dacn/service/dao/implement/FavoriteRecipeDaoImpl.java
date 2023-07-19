//package com.hcmut.dacn.service.dao.implement;
//
//import com.axonactive.entity.FavoriteRecipeEntity;
//import com.axonactive.service.dao.FavoriteRecipeDao;
//
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import java.util.List;
//
//@Stateless
//public class FavoriteRecipeDaoImpl implements FavoriteRecipeDao {
//    @PersistenceContext(name = "FoodRecipe")
//    EntityManager em;
//    @Override
//    public List<FavoriteRecipeEntity> getAllByUserId(Long userId) {
//        Query getAllByUserIdQuery = em.createQuery("SELECT m FROM FavoriteRecipeEntity m WHERE m.user.id=:userId", FavoriteRecipeEntity.class);
//        getAllByUserIdQuery.setParameter("userId",userId);
//        return getAllByUserIdQuery.getResultList();
//    }
//
//    @Override
//    public FavoriteRecipeEntity createOne(FavoriteRecipeEntity favoriteRecipe) {
//        return em.merge(favoriteRecipe);
//    }
//}
