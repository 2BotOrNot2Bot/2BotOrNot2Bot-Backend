package com.turing.test.controller;

import com.turing.test.domain.User;
import com.turing.test.service.UserService;
import com.turing.test.service.dto.UserDto;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 0:31
**/
@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResultVo<UserDto> findUser(@RequestParam String email) throws InterruptedException, ExecutionException{
        log.info("UserController->findUser: {}",email);
        return userService.findUser(email);
    }

    @PostMapping("/users")
    public ResultVo<String> addUser(@RequestBody User user) throws InterruptedException, ExecutionException {
        return userService.addUser(user);
    }

}