//package com.hcmut.dacn.service.dao.implement;
//
//import com.axonactive.entity.CommentEntity;
//import com.axonactive.service.dao.CommentDao;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Component;
//
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import java.util.List;
//
//@Component
//public class CommentDaoImpl implements CommentDao {
//    @PersistenceContext(name = "FoodRecipe")
//    EntityManager em;
//
//    @Override
//    public List<CommentEntity> getAllByEvaluationId(Long evaluationId){
//        Query getAllByEvaluationIdQuery=em.createQuery("SELECT c FROM CommentEntity c WHERE c.evaluation.id=:evaluationId ORDER BY c.id DESC", CommentEntity.class);
//        getAllByEvaluationIdQuery.setParameter("evaluationId",evaluationId);
//        return getAllByEvaluationIdQuery.getResultList();
//    }
//    @Override
//    public CommentEntity create(CommentEntity comment){
//        return em.merge(comment);
//    }
//}
