package com.turing.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turing.test.domain.Chatbot;
import com.turing.test.domain.enums.Chatbots;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;

/**
* @Author Yibo Wen
* @Date 2021/10/24 17:28
**/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendEntryTest {

    @Autowired
    UserService userService;

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    DialogueService dialogueService;

    @Test
    void checkUpdateChatbotPercentage() throws ExecutionException, InterruptedException {
        for (Chatbots bot: Chatbots.values()){
            String chatbotName = bot.getName();
            try {
                chatbotService.clearChatbotStat(chatbotName);

//            CompletableFuture<ResultVo<Double>> result0 = chatbotService.updateChatbotStat(chatbotName,true).;
//            CompletableFuture<ResultVo<Double>> result1 = chatbotService.updateChatbotStat(chatbotName,false);
//            CompletableFuture<ResultVo<Double>> result2 = chatbotService.updateChatbotStat(chatbotName,true);
//            CompletableFuture<ResultVo<Double>> result3 = chatbotService.updateChatbotStat(chatbotName,false);
//            CompletableFuture<ResultVo<Double>> result4 = chatbotService.updateChatbotStat(chatbotName,true);
//            CompletableFuture<ResultVo<Double>> result5 = chatbotService.updateChatbotStat(chatbotName,false);
//            CompletableFuture<ResultVo<Double>> result6 = chatbotService.updateChatbotStat(chatbotName,true);
//            CompletableFuture<ResultVo<Double>> result7 = chatbotService.updateChatbotStat(chatbotName,true);
//            CompletableFuture<ResultVo<Double>> result8 = chatbotService.updateChatbotStat(chatbotName,false);
//            CompletableFuture<ResultVo<Double>> result9 = chatbotService.updateChatbotStat(chatbotName,false);
//            CompletableFuture<ResultVo<Double>> result10 = chatbotService.updateChatbotStat(chatbotName,true);
//            CompletableFuture<ResultVo<Double>> result11 = chatbotService.updateChatbotStat(chatbotName,true);
//            CompletableFuture<ResultVo<Double>> result12 = chatbotService.updateChatbotStat(chatbotName,false);
//            CompletableFuture<ResultVo<Double>> result13 = chatbotService.updateChatbotStat(chatbotName,false);
//
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,true).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
                chatbotService.updateChatbotStat(chatbotName,false).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void checkGetSortedChatbotStat() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            ResultVo<List<Chatbot>> chatbots= chatbotService.getSortedChatbotStat();
            Assertions.assertEquals("success",chatbots.getMsg());
            for(Chatbot chatbot : chatbots.getData()){
                log.info("BackendEntryTest->checkGetSortedChatbotStat: {} has achieved {}%",
                        chatbot.getName(),String.format("%.2f", chatbot.getPercentage()*100));
            }
        }
    }

    @Test
    void testClearChatBotStat(){
        for (Chatbots bot: Chatbots.values()){
            String chatbotName = bot.getName();
            try{
                ResultVo<String> result = chatbotService.clearChatbotStat(chatbotName);
                Assertions.assertEquals("success",result.getMsg());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void checkGetResponseFromDialogflow() throws IOException {
        ResultVo<String> response = dialogueService.getResponse("Hello","dialogflow", "123edf");
        Assertions.assertEquals("success",response.getMsg());
        log.info("BackendEntryTest->checkGetResponseFromDialogflow: {}",response);
    }



//    @Test
//    void haha () {
//        try {
//            HttpResponse<String> response = Unirest.get("https://acobot-brainshop-ai-v1.p.rapidapi.com/get?bid=178&key=sX5A2PcYZbsN5EY6&uid=mashape&msg=Hello!")
//                    .header("x-rapidapi-host", "acobot-brainshop-ai-v1.p.rapidapi.com")
//                    .header("x-rapidapi-key", "9d4fe34aadmsha68f858be4546dfp1c1075jsn843af4fa523e")
//                    .asString();
//            String message = JsonParser.parseString(response.getBody()).getAsJsonObject().toString();
//            message = message.substring(2,message.length()-2);
//            message = message.replace("\"", "'");
//            log.info(message);
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    void resetChatbots() throws ExecutionException, InterruptedException {
        Assertions.assertEquals(Chatbots.values().length,chatbotService.resetChatbots().getData());
    }

    @Test
    void checkRandomPick(){
        TreeMap<String, Integer> stats = new TreeMap<String, Integer>();
        for (Chatbots chatbot: Chatbots.values()){
            stats.put(chatbot.getName(), 0);
        }
        for (int i = 0; i < 10000; i++) {
            String name = Chatbots.getRandomName();
            stats.put(name, stats.get(name) + 1);
        }
        for (Map.Entry<String, Integer> e: stats.entrySet()) {
            Assertions.assertTrue(e.getValue() >= 1000);
        }
    }



}