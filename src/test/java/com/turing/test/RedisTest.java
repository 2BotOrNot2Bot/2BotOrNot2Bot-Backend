package com.turing.test;

import com.turing.test.domain.Chatbot;
import com.turing.test.domain.User;
import com.turing.test.service.ChatbotService;
import com.turing.test.service.DialogueService;
import com.turing.test.service.UserService;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
* @Author Yibo Wen
* @Date 2021/10/24 17:28
**/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class RedisTest {

    @Autowired
    DialogueService dialogueService;

    @Test
    void startSearch(){
        dialogueService.startSearch("123456");
        ResultVo<String> result = dialogueService.findOpponent("123456");
        Assertions.assertNull(result.getData());
        dialogueService.startSearch("888888");
        ResultVo<String> result1 = dialogueService.findOpponent("888888");
        Assertions.assertEquals("123456",result1.getData());
        ResultVo<String> result2 = dialogueService.findOpponent("123456");
        Assertions.assertEquals("888888",result2.getData());
    }
    
    @Test
    void checkGetResponseFromDialogflow() throws IOException {
        ResultVo<String> response = dialogueService.getResponse("Hello","dialogflow", "123edf");
        Assertions.assertEquals("success",response.getMsg());
        log.info("BackendEntryTest->checkGetResponseFromDialogflow: {}",response);
    }

}