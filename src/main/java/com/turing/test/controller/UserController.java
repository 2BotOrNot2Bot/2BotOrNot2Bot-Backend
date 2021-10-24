package com.turing.test.controller;

import com.turing.test.domain.User;
import com.turing.test.service.UserService;
import com.turing.test.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 0:31
**/
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public UserDto getPatient(@RequestParam String email) throws InterruptedException, ExecutionException{
        return userService.findUser(email);
    }

    @PostMapping("/users")
    public String createPatient(@RequestBody User user) throws InterruptedException, ExecutionException {
        return userService.addUser(user);
    }

}