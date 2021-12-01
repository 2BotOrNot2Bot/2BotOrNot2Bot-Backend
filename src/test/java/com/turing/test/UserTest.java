package com.turing.test;

import com.turing.test.service.UserService;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 11/30/2021 7:57 PM
**/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {

    @Autowired
    UserService userService;

    @Test
    void updateUser() throws InterruptedException, ExecutionException {
        String uid = "testUser";
        ResultVo<String> result = userService.addUser(uid);
        Assertions.assertEquals("success", result.getMsg());
        ResultVo<Integer> result1 = userService.updateUserPoints(uid,true);
        Assertions.assertEquals(60, result1.getData());
        ResultVo<Integer> result2 = userService.updateUserPoints(uid,false);
        Assertions.assertEquals(55, result2.getData());
        ResultVo<String> result3 = userService.deleteUser(uid); // Avoid polluting the database
    }

}