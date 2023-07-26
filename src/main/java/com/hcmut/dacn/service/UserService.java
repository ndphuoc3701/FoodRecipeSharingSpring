package com.hcmut.dacn.service;


import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.exception.AlreadyExistedEntityException;
import com.hcmut.dacn.exception.NotFoundEntityException;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.UserRequest;
import com.hcmut.dacn.dto.UserDto;
import com.hcmut.dacn.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {
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

    public UserDto create(UserRequest userRequest){
        List<UserEntity> checkIfUserExited=userRepository.findByUsername(userRequest.getUsername());
        if (!checkIfUserExited.isEmpty()){
            throw new AlreadyExistedEntityException("User already existed");
        }
        UserEntity user=new UserEntity();
        user.setFullName(userRequest.getFullName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setImageData(userRequest.getImage().getBytes(StandardCharsets.UTF_8));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto login(UserRequest userRequest){
        List<UserEntity> users= userRepository.findByUsername(userRequest.getUsername());
        if(users.isEmpty())throw new NotFoundEntityException("User not found");
        return userMapper.toDto(users.get(0));
    }
}
