package com.hcmut.dacn.controller;

import com.hcmut.dacn.request.UserRequest;
import com.hcmut.dacn.service.UserService;
import com.hcmut.dacn.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("api/users")
public class UserController {
    public final static String PATH="users";
    @Autowired
    UserService userService;

//    @GetMapping
//    public List<UserDto> getAll(){
//        return userService.getAll();
//    }
//
//    @GetMapping("{userId}")
//    public UserDto getByUserId(@PathVariable("userId") Long userId){
//        return userService.getByUserId(userId);
//    }
//
//    @GetMapping("top-reputation")
//    public List<UserDto> getTopReputationUser(){
//        return userService.getTopReputationUser();
//    }
//
//    @GetMapping("top-cook-level")
//    public List<UserDto> getTopCookLevelUser(){
//        return userService.getTopCookLeveUser();
//    }

    @GetMapping
    public Principal retrievePrincipal(Principal principal) {
        return principal;
    }
    @PostMapping
    public UserDto createUser(@RequestBody UserRequest userRequest){
        return userService.create(userRequest);
    }

    @PostMapping("login")
    public UserDto login(@RequestBody UserRequest userRequest){
        return userService.login(userRequest);
    }
}
