//package com.hcmut.dacn.service.dao.implement;
//
//import com.axonactive.entity.EvaluationEntity;
//import com.axonactive.exception.EntityConstant;
//import com.axonactive.exception.NoEntityFoundException;
//import com.axonactive.service.dao.EvaluationDao;
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
//public class EvaluationDaoImpl implements EvaluationDao {
//    @PersistenceContext(name = "FoodRecipe")
//    EntityManager em;
//    @Override
//    public List<EvaluationEntity> getAllByRecipeId(Long recipeId) {
//        Query getAllByRecipeIdQuery= em.createQuery("SELECT e FROM EvaluationEntity e WHERE e.recipe.id=:recipeId ORDER BY e.id DESC", EvaluationEntity.class);
//        getAllByRecipeIdQuery.setParameter("recipeId",recipeId);
//        return  getAllByRecipeIdQuery.getResultList();
//    }
//    @Override
//    public EvaluationEntity create(EvaluationEntity evaluation){
//        return em.merge(evaluation);
//    }
//    @Override
//    public EvaluationEntity getByEvaluationId(Long evaluationId){
//        Query getByEvaluationId= em.createQuery("SELECT e FROM EvaluationEntity e WHERE e.id=:evaluationId", EvaluationEntity.class);
//        getByEvaluationId.setParameter("evaluationId",evaluationId);
//        try {
//            return (EvaluationEntity) getByEvaluationId.getSingleResult();
//        }
//        catch (NoResultException e){
//            throw new NoEntityFoundException(EntityConstant.EVALUATION, Response.Status.NOT_FOUND);
//        }
//    }
//}
