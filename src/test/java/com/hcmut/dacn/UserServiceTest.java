package com.hcmut.dacn;

import com.hcmut.dacn.dto.UserDto;
import com.hcmut.dacn.entity.EvaluationEntity;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.exception.AlreadyExistedEntityException;
import com.hcmut.dacn.exception.NotFoundEntityException;
import com.hcmut.dacn.mapper.UserMapper;
import com.hcmut.dacn.repository.UserRepository;
import com.hcmut.dacn.request.UserRequest;
import com.hcmut.dacn.service.UserService;
import org.apache.catalina.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    private final ArgumentCaptor<UserEntity> userArgumentCaptor=ArgumentCaptor.forClass(UserEntity.class);

    @Test
    public void testCreateUser(){
        when(userRepository.findByUsername("u1")).thenReturn(null);
        UserRequest userRequest=new UserRequest();
        userRequest.setUsername("u1");
        userRequest.setImage("image");
        doReturn(new UserEntity()).when(userRepository).save(userArgumentCaptor.capture());
        userService.create(userRequest);
        UserEntity user= userArgumentCaptor.getValue();
        Assert.assertEquals(user.getUsername(),"u1");
    }

    @Test(expected = AlreadyExistedEntityException.class)
    public void testCreateUserExisted(){
        when(userRepository.findByUsername("u1")).thenReturn(new UserEntity());
        UserRequest userRequest=new UserRequest();
        userRequest.setUsername("u1");
        userRequest.setImage("image");
        doReturn(new UserEntity()).when(userRepository).save(userArgumentCaptor.capture());
        userService.create(userRequest);
        UserEntity user= userArgumentCaptor.getValue();
        Assert.assertEquals(user.getUsername(),"u1");
    }

    @Test
    public void testUpdateImage(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));
        UserDto userDto=new UserDto();
        userDto.setId(1L);
        userDto.setImage("image");
        doReturn(new UserEntity()).when(userRepository).save(userArgumentCaptor.capture());
        userService.updateImage(userDto);
        Assert.assertEquals(new String(userArgumentCaptor.getValue().getImageData(),StandardCharsets.UTF_8) ,userDto.getImage());
    }

    @Test
    public void testGetUserInfo(){
        UserEntity user=new UserEntity();
        user.setFullName("name");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doReturn(new UserDto()).when(userMapper).toDto(userArgumentCaptor.capture());
        userService.getUserInfo(1L);
        Assert.assertEquals(userArgumentCaptor.getValue().getFullName(),"name");
    }

    @Test
    public void testLogin(){
        UserEntity user=new UserEntity();
        user.setUsername("u1");
        UserRequest userRequest=new UserRequest();
        userRequest.setUsername("u1");
        when(userRepository.findByUsername("u1")).thenReturn(user);
        doReturn(new UserDto()).when(userMapper).toDto(userArgumentCaptor.capture());
        userService.login(userRequest);
        Assert.assertEquals(userArgumentCaptor.getValue().getUsername(),"u1");
    }

    @Test(expected = NotFoundEntityException.class)
    public void testLogin2(){
        UserRequest userRequest=new UserRequest();
        userRequest.setUsername("u1");
        when(userRepository.findByUsername("u1")).thenReturn(null);
        userService.login(userRequest);
        Assert.assertEquals(userArgumentCaptor.getValue().getUsername(),"u1");
    }
}
