package com.turing.test;

import com.turing.test.service.DialogueService;
import com.turing.test.service.util.RedisUtils;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 17:28
**/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisTest {

    @Autowired
    DialogueService dialogueService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    StringRedisTemplate redisTemplate;


    @Test
    void startSearch() throws InterruptedException {
        dialogueService.startSearch("123456");
        ResultVo<Pair<String,String>> result = dialogueService.findOpponent("123456");
        Assertions.assertNull(result.getData());
        dialogueService.startSearch("888888");
        ResultVo<Pair<String,String>> result1 = dialogueService.findOpponent("888888");
        Assertions.assertEquals("123456",result1.getData().getFirst());
        ResultVo<Pair<String,String>> result2 = dialogueService.findOpponent("123456");
        Assertions.assertEquals("888888",result2.getData().getFirst());
        Thread.sleep(1000L);
        dialogueService.endDialogue("123456");
        dialogueService.endDialogue("888888");
    }
    
    @Test
    void checkGetResponseFromDialogflow() throws IOException {
        ResultVo<String> response = dialogueService.getResponse("Hello","dialogflow", "123edf");
        Assertions.assertEquals("success",response.getMsg());
        log.info("BackendEntryTest->checkGetResponseFromDialogflow: {}",response);
    }

    @Test
    void checkClearCache() throws IOException {
        dialogueService.startSearch("123456");
        dialogueService.startSearch("888888");
        Assertions.assertNotEquals(0,redisTemplate.getConnectionFactory().getConnection().keys("*".getBytes()).size());
        dialogueService.findOpponent("888888");
        dialogueService.findOpponent("123456");
        Assertions.assertNotEquals(0,redisTemplate.getConnectionFactory().getConnection().keys("*".getBytes()).size());
        redisUtils.clearAll();
        Assertions.assertEquals(0,redisTemplate.getConnectionFactory().getConnection().keys("*".getBytes()).size());
    }

}