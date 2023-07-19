//package com.hcmut.dacn.service.dao.implement;
//
//import com.axonactive.entity.RecipeEntity;
//import com.axonactive.exception.EntityConstant;
//import com.axonactive.exception.NoEntityFoundException;
//import com.axonactive.service.dao.RecipeDao;
//
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.ws.rs.core.Response;
//import java.util.List;
//
//@Stateless
//public class RecipeDaoImpl implements RecipeDao {
//    @PersistenceContext(name = "FoodRecipe")
//    EntityManager em;
//
//    @Override
//    public List<RecipeEntity> getAll(){
//        Query getAllQuery=em.createQuery("SELECT r FROM RecipeEntity r", RecipeEntity.class);
//        return getAllQuery.getResultList();
//    }
//    @Override
//    public RecipeEntity getByRecipeId(Long recipeId){
//        Query getByRecipeIdQuery = em.createQuery("SELECT r FROM RecipeEntity r WHERE r.id=:recipeId", RecipeEntity.class);
//        getByRecipeIdQuery.setParameter("recipeId", recipeId);
//        try {
//            return (RecipeEntity) getByRecipeIdQuery.getSingleResult();
//        }
//        catch (NoResultException e){
//            throw new NoEntityFoundException(EntityConstant.RECIPE, Response.Status.NOT_FOUND);
//        }
//    }
//
////    Need find owner first?
//    @Override
//    public List<RecipeEntity> getAllByOwnerId(Long ownerId){
//        Query getAllByOwnerIdQuery = em.createQuery("SELECT r FROM RecipeEntity r WHERE r.owner.id=:ownerId", RecipeEntity.class);
//        getAllByOwnerIdQuery.setParameter("ownerId", ownerId);
//        return getAllByOwnerIdQuery.getResultList();
//    }
//
//    @Override
//    public RecipeEntity create(RecipeEntity recipe) {
//        return em.merge(recipe);
//    }
//}
