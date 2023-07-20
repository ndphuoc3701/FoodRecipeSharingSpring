package com.hcmut.dacn.service;


import com.hcmut.dacn.entity.ImageInstructionEntity;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.UserRequest;
import com.hcmut.dacn.service.dao.UserDao;
import com.hcmut.dacn.service.dto.UserDto;
import com.hcmut.dacn.service.mapper.UserMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {
//    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    public List<UserDto> getAll(){
//        return userMapper.toDtos(userDao.getAll());
        return userMapper.toDtos(userRepository.findAll());
    }
    public UserDto getByUserId(Long userId){
//        return userMapper.toDto(userDao.getByUserId(userId));
        return userMapper.toDto(userRepository.findById(userId).orElse(null));
    }
    public List<UserDto> getTopReputationUser(){
        return userMapper.toDtos(userDao.getTopReputationUser());
    }
    public List<UserDto> getTopCookLeveUser(){
        return userMapper.toDtos(userDao.getTopCookLevelUser());
    }

    public UserDto create(UserRequest userRequest){
        UserEntity user=new UserEntity();
        user.setFullName(userRequest.getFullName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setImageData(userRequest.getImage().getBytes(StandardCharsets.UTF_8));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto login(UserRequest userRequest){
        List<UserEntity> user= userRepository.findByUsername(userRequest.getUsername());
        return userMapper.toDto(userRepository.save(user.get(0)));
    }
}
