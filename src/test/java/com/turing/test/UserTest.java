package com.turing.test;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.turing.test.service.UserService;
import com.turing.test.service.impl.UserServiceImpl;
import com.turing.test.vo.ResultVo;
import io.lettuce.core.ScriptOutputType;
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
    void testFindUser() throws InterruptedException {
        try{
            ResultVo<Integer> user = userService.findUser("bJ1vXhCSr5aFnzJeonB2eGResqv1");
            Assertions.assertEquals("success",user.getMsg());
        }catch (ExecutionException e){
            System.out.println("findUser failed");
        }

    }

    @Test
    void testAddUser() throws InterruptedException {
        String uid = "testUser";
        try{
            ResultVo<String> result = userService.addUser(uid);
            Assertions.assertEquals("success", result.getMsg());
            ResultVo<Integer> user = userService.findUser("testUser");
            Assertions.assertEquals("success",user.getMsg());
            Assertions.assertEquals(50, user.getData());
        }catch (ExecutionException e){
            System.out.println("addUser Failed:"+e.getMessage());
        }
        try{
            ResultVo<String> result3 = userService.deleteUser(uid);
            Assertions.assertEquals("success", result3.getMsg());
        }catch (ExecutionException e){
            System.out.println("delete unsuccessful");
        }
    }

    @Test
    void testUpdateUser() throws InterruptedException{
        String uid = "testUser";
        try{
            ResultVo<String> result = userService.addUser(uid);
            Assertions.assertEquals("success", result.getMsg());
            ResultVo<Integer> result1 = userService.updateUserPoints(uid,true);
            Assertions.assertEquals(60, result1.getData());
            ResultVo<Integer> result2 = userService.updateUserPoints(uid,false);
            Assertions.assertEquals(55, result2.getData());
            ResultVo<String> result3 = userService.deleteUser(uid); // Avoid polluting the database
        }catch (ExecutionException e){
            System.out.println("update unsuccessful");
        }
    }
}