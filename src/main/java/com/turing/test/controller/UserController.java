package com.turing.test.controller;

import com.turing.test.domain.User;
import com.turing.test.service.UserService;
import com.turing.test.vo.BusinessError;
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
    public ResultVo<User> findUser(@RequestParam String uid) throws InterruptedException, ExecutionException{
        log.info("UserController->findUser: {}",uid);
        return userService.findUser(uid);
    }

    @PostMapping("/users")
    public ResultVo<String> addUser(@RequestParam String uid) throws InterruptedException, ExecutionException {
        log.info("UserController->addUser: {}",uid);
        return userService.addUser(uid);
    }

    @PatchMapping("/users")
    public ResultVo<Integer> updateUser(@RequestParam String uid, @RequestParam Boolean result) throws ExecutionException, InterruptedException {
        log.info("UserController->updateUser: {} answer {}",uid,result);
        return userService.updateUserPoints(uid,result);
    }

}