//package com.hcmut.dacn.service.dao.implement;
//
//import com.axonactive.entity.UserEntity;
//import com.axonactive.exception.EntityConstant;
//import com.axonactive.exception.NoEntityFoundException;
//import com.axonactive.service.dao.UserDao;
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
//public class UserDaoImpl implements UserDao {
//    @PersistenceContext(name = "FoodRecipe")
//    EntityManager em;
//    @Override
//    public List<UserEntity> getAll(){
//        Query allUserQuery=em.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
//        return allUserQuery.getResultList();
//    }
//    @Override
//    public List<UserEntity> getTopReputationUser() {
//        Query topReputationUserQuery=em.createNamedQuery("User_GetTopReputation",UserEntity.class);
//        return topReputationUserQuery.setMaxResults(50).getResultList();
//    }
//
//    @Override
//    public List<UserEntity> getTopCookLevelUser() {
//        Query topCookLevelUserQuery=em.createNamedQuery("User_GetTopCookLevel",UserEntity.class);
//        return topCookLevelUserQuery.setMaxResults(50).getResultList();
//    }
//
//    @Override
//    public UserEntity getByUserId(Long userId){
//        Query oneUserQuery=em.createNamedQuery("User_GetById", UserEntity.class);
//        oneUserQuery.setParameter("userId",userId);
//        try {
//            return (UserEntity) oneUserQuery.getSingleResult();
//        }
//        catch (NoResultException e){
//            throw new NoEntityFoundException(EntityConstant.USER, Response.Status.NOT_FOUND);
//        }
//    }
//
//}
