package controller;

import com.hcmut.dacn.service.UserService;
import com.hcmut.dacn.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    public final static String PATH="users";
    @Autowired
    UserService userService;

    @GetMapping
    public List<UserDto> getAll(){
        return userService.getAll();
    }

    @GetMapping("{userId}")
    public UserDto getByUserId(@PathVariable("userId") Long userId){
        return userService.getByUserId(userId);
    }

    @GetMapping("top-reputation")
    public List<UserDto> getTopReputationUser(){
        return userService.getTopReputationUser();
    }

    @GetMapping("top-cook-level")
    public List<UserDto> getTopCookLevelUser(){
        return userService.getTopCookLeveUser();
    }
}
