package com.turing.test.service;

import com.turing.test.domain.User;
import com.turing.test.service.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/9/23 21:45
**/
@Service
public interface UserService {

    UserDto findUser(String email) throws InterruptedException, ExecutionException;

    String addUser(User user) throws InterruptedException, ExecutionException;

}
