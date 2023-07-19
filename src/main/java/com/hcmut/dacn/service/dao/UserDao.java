package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.UserEntity;

import java.util.List;

//@Stateless
public interface UserDao {
    UserEntity getByUserId(Long userId);
    List<UserEntity> getAll();
    List<UserEntity> getTopReputationUser();
    List<UserEntity> getTopCookLevelUser();
}
