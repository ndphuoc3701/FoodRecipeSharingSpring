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
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDto create(UserRequest userRequest){
        UserEntity checkIfUserExited=userRepository.findByUsername(userRequest.getUsername());
        if (checkIfUserExited!=null){
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
        UserEntity user= userRepository.findByUsername(userRequest.getUsername());
        if(user==null || !Objects.equals(userRequest.getPassword(), user.getPassword()))
            throw new NotFoundEntityException("User not found");
        return userMapper.toDto(user);
    }

    public void updateImage(UserDto userDto){
        UserEntity user=userRepository.findById(userDto.getId()).orElse(null);
        user.setImageData(userDto.getImage().getBytes(StandardCharsets.UTF_8));
        userRepository.save(user);
    }

    public UserDto getUserInfo(Long userId){
        UserEntity user=userRepository.findById(userId).orElse(null);
        return userMapper.toDto(user);
    }
}
