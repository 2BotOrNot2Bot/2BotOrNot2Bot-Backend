package com.turing.test.service;

import com.turing.test.domain.User;
import com.turing.test.vo.ResultVo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/9/23 21:45
**/
@Service
public interface UserService {

    ResultVo<User> findUser(String firebaseUid) throws InterruptedException, ExecutionException;

    ResultVo<String> addUser(String firebaseUid) throws InterruptedException, ExecutionException;
}
