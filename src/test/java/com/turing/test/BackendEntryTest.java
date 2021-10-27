package com.turing.test;

import com.turing.test.domain.Chatbot;
import com.turing.test.service.ChatbotService;
import com.turing.test.service.UserService;
import com.turing.test.service.dto.UserDto;
import com.turing.test.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.*;

/**
* @Author Yibo Wen
* @Date 2021/10/24 17:28
**/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class BackendEntryTest {

    @Autowired
    UserService userService;

    @Autowired
    ChatbotService chatbotService;

    @Test
    void findUser() throws ExecutionException, InterruptedException {
        ResultVo<UserDto> user = userService.findUser("shawn@usc.edu");
        Assertions.assertEquals("success",user.getMsg());
    }

    @Test
    void checkUpdateChatbotPercentage() throws ExecutionException, InterruptedException {
        try {
            chatbotService.clearChatbotStat("testbot");

//            CompletableFuture<ResultVo<Double>> result0 = chatbotService.updateChatbotStat("testbot",true).;
//            CompletableFuture<ResultVo<Double>> result1 = chatbotService.updateChatbotStat("testbot",false);
//            CompletableFuture<ResultVo<Double>> result2 = chatbotService.updateChatbotStat("testbot",true);
//            CompletableFuture<ResultVo<Double>> result3 = chatbotService.updateChatbotStat("testbot",false);
//            CompletableFuture<ResultVo<Double>> result4 = chatbotService.updateChatbotStat("testbot",true);
//            CompletableFuture<ResultVo<Double>> result5 = chatbotService.updateChatbotStat("testbot",false);
//            CompletableFuture<ResultVo<Double>> result6 = chatbotService.updateChatbotStat("testbot",true);
//            CompletableFuture<ResultVo<Double>> result7 = chatbotService.updateChatbotStat("testbot",true);
//            CompletableFuture<ResultVo<Double>> result8 = chatbotService.updateChatbotStat("testbot",false);
//            CompletableFuture<ResultVo<Double>> result9 = chatbotService.updateChatbotStat("testbot",false);
//            CompletableFuture<ResultVo<Double>> result10 = chatbotService.updateChatbotStat("testbot",true);
//            CompletableFuture<ResultVo<Double>> result11 = chatbotService.updateChatbotStat("testbot",true);
//            CompletableFuture<ResultVo<Double>> result12 = chatbotService.updateChatbotStat("testbot",false);
//            CompletableFuture<ResultVo<Double>> result13 = chatbotService.updateChatbotStat("testbot",false);
//
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",false).get();
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",false).get();
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",false).get();
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",false).get();
            chatbotService.updateChatbotStat("testbot",false).get();
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",true).get();
            chatbotService.updateChatbotStat("testbot",false).get();
            chatbotService.updateChatbotStat("testbot",false).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkGetSortedChatbotStat() throws ExecutionException, InterruptedException {
        ResultVo<List<Chatbot>> chatbots= chatbotService.getSortedChatbotStat();
        Assertions.assertEquals("success",chatbots.getMsg());
        for(Chatbot chatbot : chatbots.getData()){
            log.info("BackendEntryTest->checkGetSortedChatbotStat: {} has achieved {}%",
                    chatbot.getName(),String.format("%.2f", chatbot.getPercentage()*100));
        }
    }

}